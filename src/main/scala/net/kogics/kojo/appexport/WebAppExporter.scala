package net.kogics.kojo.appexport

import java.io._
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream

import scala.util.parsing.json._
import scala.util.Using

import net.kogics.kojo.util.Unzipper

object WebAppExporter {
  def run(script: String): Unit = {
    println("Web-compiling via iKojo...")
    val url = "http://ikojo.in:8880/compile?opt=fast"
    val postDataStr = s"""
import fiddle.Fiddle.println
import scalajs.js

@js.annotation.JSExportTopLevel("ScalaFiddle")
object ScalaFiddle {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

  // $$FiddleStart
$script
  // $$FiddleEnd
}
"""

    var con: HttpURLConnection = null
    try {
      val myurl = new URL(url)
      con = myurl.openConnection().asInstanceOf[HttpURLConnection]

      con.setDoOutput(true)
      con.setRequestMethod("POST")
      con.setRequestProperty("User-Agent", "Java client")
      con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
      con.setRequestProperty("Accept", "*/*")
      con.setRequestProperty("Accept-Encoding", "gzip")

      println("Sending script...")
      val scriptBytes = postDataStr.getBytes(StandardCharsets.UTF_8)
      Using(new DataOutputStream(con.getOutputStream())) { wr =>
        wr.write(scriptBytes)
      }

      println("Reading response...")
      //      println(s"Response code: ${con.getResponseCode}")
      println(s"Response message: ${con.getResponseMessage}")
      println("Reading response body...")
      val content = new StringBuilder()
      Using(new BufferedReader(new InputStreamReader(new GZIPInputStream(con.getInputStream())))) { br =>
        var line = br.readLine()
        while (line != null) {
          content.append(line)
          content.append(System.lineSeparator())
          line = br.readLine()
        }
      }
      println("Script compilation done.")

      @annotation.nowarn
      def parseJson(json: String) = JSON.parseFull(json)

      println("Parsing response...")
      val parsed = parseJson(content.toString)
      println("Parsing done.")
      parsed match {
        case Some(parsedData) =>
          val parsedMap = parsedData.asInstanceOf[Map[Any, Any]]
          val jsCodeSeq = parsedMap("jsCode").asInstanceOf[List[String]]
          if (jsCodeSeq.length > 0) {
            val home = System.getProperty("user.home")
            val exportDir = s"$home/kojo-export"
            val displayExportDir = new File(exportDir).getCanonicalPath
            println(s"Downloading and extracting Web-App template to $displayExportDir...")
            val templateUrl = new URL("https://docs.kogics.net/assets/files/webapp.zip")
            Unzipper.unzipUrl(templateUrl, exportDir)
            println("Template downloading and extracting done.")

            println("Writing JavaScript file...")
            val file = new File(s"$exportDir/webapp/target/scala-2.12/kojo-dev-fastopt.js")
            val bw = new BufferedWriter(new FileWriter(file))
            bw.write(jsCodeSeq.head)
            bw.close()
            println("Export done.")
            println("---")
            println(s"The exported Web-App is available at $displayExportDir")
            println("---")
            println("For information on what to do next, check out - https://docs.kogics.net/howtos/webapp-export.html")
          }
          else {
            println("Compilation error:")
            println(parsedMap("log"))
            println("Make sure the program works in iKojo before trying to export it.")
          }
        case None => println("Error in response.")
      }
    }
    catch {
      case t: Throwable =>
        println(s"Problem: ${t.getClass.getName} - ${t.getMessage}")
    }
    finally {
      con.disconnect()
    }
  }
}
