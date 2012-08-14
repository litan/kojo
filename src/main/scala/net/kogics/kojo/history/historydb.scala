/*
 * Copyright (C) 2012 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */
package net.kogics.kojo.history

import java.sql.DriverManager
import java.io.File

// code to simplify jdbc queries from: 
// http://zcox.wordpress.com/2009/08/17/simple-jdbc-queries-in-scala/
object Control {
  def using[Closeable <: { def close(): Unit }, B](closeable: Closeable)(getB: Closeable => B): B =
    try {
      getB(closeable)
    } finally {
      closeable.close()
    }

  import scala.collection.mutable.ListBuffer

  def bmap[T](test: => Boolean)(block: => T): List[T] = {
    val ret = new ListBuffer[T]
    while (test) ret += block
    ret.toList
  }

  import java.sql._

  /** Executes the SQL and processes the result set using the specified function. */
  def query[B](connection: Connection, sql: String)(process: ResultSet => B): B =
    //    using (connection) { connection => 
    using(connection.createStatement) { statement =>
      using(statement.executeQuery(sql)) { results =>
        process(results)
      }
    }
  //    }

  /** Executes the SQL and uses the process function to convert each row into a T. */
  def queryEach[T](connection: Connection, sql: String)(process: ResultSet => T): List[T] =
    query(connection, sql) { results =>
      bmap(results.next) {
        process(results)
      }
    }
}

class DBHistorySaver extends HistorySaver {

  var needCreate = false
  val userDir = System.getProperty("user.home")
  if (!new File(userDir + "/.kojo/lite/db").exists) {
    println("\nCreating Kojo Database...\nKojo conf Dir: %s/.kojo" format (userDir))
    needCreate = true
  }

  import java.sql._
  Class.forName("org.h2.Driver")
  val conn = DriverManager.getConnection("jdbc:h2:~/.kojo/lite/db/kojo", "sa", "");
  // need to think about connection closing and possible timeout
  conn.setAutoCommit(true)

  if (needCreate) {
    createTable()
  }

  import Control._

  def createTable() {
    using(conn.createStatement) { statement =>
      statement.executeUpdate("""CREATE TABLE HISTORY(ID IDENTITY PRIMARY KEY, 
          SCRIPT CLOB, 
          STARRED BOOLEAN, 
          TAGS VARCHAR(1024), 
          AT TIMESTAMP)""")
    }
    // ALTER TABLE HISTORY ADD COLUMN IF NOT EXISTS USER VARCHAR(255)  
  }

  def deleteTable() {
    using(conn.createStatement) { statement =>
      statement.executeUpdate("DROP TABLE IF EXISTS HISTORY")
    }
  }

  def readAll() = {
    queryEach(conn, "SELECT * FROM HISTORY ORDER BY AT DESC LIMIT 1000") { rs =>
      HistoryItem(rs.getString("SCRIPT"), rs.getLong("ID"), rs.getBoolean("STARRED"), rs.getString("TAGS"), rs.getDate("AT"))
    }
  }

  def save(code: String): HistoryItem = {
    val h = HistoryItem(code)
    using(conn.prepareStatement("INSERT INTO HISTORY(SCRIPT, STARRED, TAGS, AT) VALUES(?, ?, ?, ?)")) { statement =>
      statement.setString(1, h.script)
      statement.setBoolean(2, h.starred)
      statement.setString(3, h.tags)
      statement.setTimestamp(4, new Timestamp(h.at.getTime))
      statement.executeUpdate()
    }
    // read h from db for new id
    h
  }
}

