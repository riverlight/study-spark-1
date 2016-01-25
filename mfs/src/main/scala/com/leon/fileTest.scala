package com.leon

import scala.io.Source

/**
  * Created by leon on 2016/1/25.
  */
object fileTest {
  private var dbHost : String = "localhost";
  private var dbPort : Int = 27017;
  private var dbUserName : String = "leon";
  private var dbPassword : String = "123";
  private var dbDatabase : String = "sca";
  private var dbCollName : String = "mysca";
  private var flHostname : String = "localhost";
  private var flPort : Int = 11114;

  def printConf(): Unit = {
    println(dbHost + " " + dbPort + " " + dbUserName + " " + dbPassword + " " + dbCollName + " " + flHostname + " " + flPort)
  }

  def main(args: Array[String]): Unit = {
    println("Hi, this is a scala file IO test program!")
    getConfig("d:\\1.cfg");
    printConf()
  }

  def getConfig(confFile : String) : Unit = {
    for ( line <- Source.fromFile(confFile).getLines()) {
      val keyVal = line.split("=")
      keyVal(0) match {
        case "dbHost" => dbHost = keyVal(1);
        case "dbPort" => dbPort = keyVal(1).toInt;
        case "dbUserName" => dbUserName = keyVal(1);
        case "dbPassword" => dbPassword = keyVal(1);
        case "dbDatabase" => dbDatabase = keyVal(1);
        case "dbCollName" => dbCollName = keyVal(1);
        case "flHostname" => flHostname = keyVal(1);
        case "flPort" => flPort = keyVal(1).toInt;
      }
    }
  }
}
