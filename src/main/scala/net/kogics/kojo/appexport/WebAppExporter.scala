package net.kogics.kojo.appexport

import java.io._
import java.net.{HttpURLConnection, URL}
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream

import net.kogics.kojo.util.Unzipper

import scala.util.Using
import scala.util.parsing.json._

object WebAppExporter {
  def run(script: String): Unit = {
    println("Web-compiling via iKojo")
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
    val postData = postDataStr.getBytes(StandardCharsets.UTF_8)

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

      println("Sending script")
      Using(new DataOutputStream(con.getOutputStream())) { wr =>
        wr.write(postData)
      }

      println("Reading response")
      //      println(s"Response code: ${con.getResponseCode}")
      println(s"Response message: ${con.getResponseMessage}")
      println("Reading response body")
      val content = new StringBuilder()
      Using(new BufferedReader(new InputStreamReader(new GZIPInputStream(con.getInputStream())))) { br =>
        var line = br.readLine()
        while (line != null) {
          content.append(line)
          content.append(System.lineSeparator())
          line = br.readLine()
        }
      }
      println("Done")

      val parsed = JSON.parseFull(content.toString)
      parsed match {
        case Some(e: Map[Any, Any]) =>
          val jsCodeSeq = e("jsCode").asInstanceOf[List[String]]
          if (jsCodeSeq.length > 0) {
            val home = System.getProperty("user.home")
            val exportDir = s"$home/kojo-export"
            println(s"Extracting Web-App template to $exportDir")
            Unzipper.unzipResource("/export/webapp.zip", exportDir)
            println("Done")

            println("Writing JavaScript file")
            val file = new File(s"$exportDir/webapp/target/scala-2.12/kojo-dev-fastopt.js")
            val bw = new BufferedWriter(new FileWriter(file))
            bw.write(jsCodeSeq.head)
            bw.close()
            println("Done")
            println(s"The exported Web-App is available at $exportDir/webapp")
          }
          else {
            println("Compilation error:")
            println(e("log"))
            println("Make sure the program works in iKojo before trying to export it")
          }
        case None => println("Error reading response.")
      }
    }
    finally {
      con.disconnect()
    }
  }
}
