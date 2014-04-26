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

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Timestamp
import java.sql.Types

import scala.collection.mutable.ListBuffer

import net.kogics.kojo.core.HistoryItem
import net.kogics.kojo.core.HistorySaver

// code to simplify jdbc queries from: 
// http://zcox.wordpress.com/2009/08/17/simple-jdbc-queries-in-scala/
object Control {
  import language.reflectiveCalls
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
  Class.forName("org.h2.Driver")
  val conn = DriverManager.getConnection("jdbc:h2:~/.kojo/lite/db/kojo", "sa", "");
  // need to think about connection closing and possible timeout
  conn.setAutoCommit(true)
  createTableIfNeeded()

  val saveStatement = conn.prepareStatement("INSERT INTO HISTORY(SCRIPT, FILE, STARRED, TAGS, AT) VALUES(?, ?, ?, ?, ?)")
  val idCall = conn.prepareCall("{? = CALL IDENTITY()}")
  idCall.registerOutParameter(1, Types.BIGINT);
  val updateStars = conn.prepareStatement("UPDATE HISTORY SET STARRED = ? WHERE ID = ?")
  val updateTags = conn.prepareStatement("UPDATE HISTORY SET TAGS = ? WHERE ID = ?")

  import Control._

  def createTableIfNeeded() {
    using(conn.createStatement) { statement =>
      statement.executeUpdate("""CREATE TABLE IF NOT EXISTS HISTORY(ID IDENTITY PRIMARY KEY, 
          SCRIPT CLOB, 
          STARRED BOOLEAN,
          FILE VARCHAR(1024),
          TAGS VARCHAR(1024), 
          AT TIMESTAMP)""")

      statement.executeUpdate("CREATE INDEX IF NOT EXISTS IDXFILE ON HISTORY(FILE)")
    }

    // ALTER TABLE HISTORY ADD COLUMN IF NOT EXISTS USER VARCHAR(255)
    // CREATE INDEX IF NOT EXISTS IDXFILE ON HISTORY(FILE)
  }

  def deleteTable() {
    using(conn.createStatement) { statement =>
      statement.executeUpdate("DROP TABLE IF EXISTS HISTORY")
    }
  }

  def readAll() = {
    queryEach(conn, "SELECT * FROM HISTORY ORDER BY AT DESC LIMIT 1000") { rs =>
      HistoryItem(rs.getString("SCRIPT"), rs.getString("FILE"), rs.getLong("ID"), rs.getBoolean("STARRED"), rs.getString("TAGS"), rs.getTimestamp("AT"))
    }
  }

  def readSome(filter: String) = {
    queryEach(conn, s"SELECT * FROM HISTORY WHERE SCRIPT LIKE '%$filter%' OR FILE LIKE '%$filter%' OR TAGS LIKE '%$filter%' ORDER BY AT DESC LIMIT 1000") { rs =>
      HistoryItem(rs.getString("SCRIPT"), rs.getString("FILE"), rs.getLong("ID"), rs.getBoolean("STARRED"), rs.getString("TAGS"), rs.getTimestamp("AT"))
    }
  }

  def save(code: String, file: Option[String]): HistoryItem = {
    val h = HistoryItem(code, file.getOrElse(""))
    saveStatement.setString(1, h.script)
    saveStatement.setString(2, h.file)
    saveStatement.setBoolean(3, h.starred)
    saveStatement.setString(4, h.tags)
    saveStatement.setTimestamp(5, new Timestamp(h.at.getTime))
    saveStatement.executeUpdate()

    // load generated id
    idCall.execute()
    h.id = idCall.getLong(1)
    h
  }
  
  def updateStar(hi: HistoryItem) {
    updateStars.setBoolean(1, hi.starred)
    updateStars.setLong(2, hi.id)
    updateStars.executeUpdate()
  }
  
  def updateTags(hi: HistoryItem) {
    updateTags.setString(1, hi.tags)
    updateTags.setLong(2, hi.id)
    updateTags.executeUpdate()
  }
  
}

