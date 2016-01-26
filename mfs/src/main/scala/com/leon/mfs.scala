package com.leon

import com.mongodb.ServerAddress
import com.mongodb.casbah.{MongoCredential, MongoClient}
import com.mongodb.casbah.commons.MongoDBObject
import org.apache.spark.streaming.flume._
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.Seconds
import org.apache.spark.storage.StorageLevel

import scala.io.Source

/**
  * Created by leon on 2016/1/21.
  */
object mfs {
  private var dbHost : String = "localhost";
  private var dbPort : Int = 27017;
  private var dbUserName : String = "leon";
  private var dbPassword : String = "123";
  private var dbDatabase : String = "sca";
  private var dbCollName : String = "mysca";
  private var flHostname : String = "localhost";
  private var flPort : Int = 11114;
  var zkQuorum = "10.10.69.166:2181";
  var consumerGroup = "test-consumers";
  var topics = "test";

  def getConfig(confFile : String) : Unit = {
    for (line <- Source.fromFile(confFile).getLines()) {
      val keyVal = line.split("=")
      println(keyVal(0))
      keyVal(0) match {
        case "dbHost" => dbHost = keyVal(1);
        case "dbPort" => dbPort = keyVal(1).toInt;
        case "dbUserName" => dbUserName = keyVal(1);
        case "dbPassword" => dbPassword = keyVal(1);
        case "dbDatabase" => dbDatabase = keyVal(1);
        case "dbCollName" => dbCollName = keyVal(1);
        case "flHostname" => flHostname = keyVal(1);
        case "flPort" => flPort = keyVal(1).toInt;
        case "zkQuorum" => zkQuorum = keyVal(1);
        case "consumerGroup" => consumerGroup = keyVal(1);
        case "topics" => topics = keyVal(1);
      }
    }
  }

  def printConf(): Unit = {
    println(dbHost + " " + dbPort + " " + dbUserName + " " + dbPassword + " " + dbCollName + " " + flHostname + " " + flPort)
  }

  def main(args: Array[String]): Unit = {
    println("Hi, this is a mongodb+flume+spark demo program")

    if (args.length < 1) {
      print("please enter config file")
      System.exit(1)
    }
    getConfig(args(0))
    printConf()
    //System.exit(1)

    val server = new ServerAddress(dbHost, dbPort)
    val credentials = MongoCredential.createMongoCRCredential(dbUserName, dbDatabase, dbPassword.toArray)
    val mongoClient = MongoClient(server, List(credentials))
    val db = mongoClient(dbDatabase)
    db.collectionNames
    val mysca = db(dbCollName)

    val sc = new SparkConf().setAppName("FlumeEventCount")
    val ssc = new StreamingContext(sc, Seconds(20))

    val storageLevel = StorageLevel.MEMORY_ONLY
    val consumerThreadsPerInputStream = 1

    println("\n" + zkQuorum + " " + consumerGroup)
    val topicMap = topics.split(",").map((_, 1.toInt)).toMap
    val kafkaStream = KafkaUtils.createStream(ssc, zkQuorum, consumerGroup, topicMap);
    kafkaStream.count().map(cnt => "Received " + cnt + " events.\n").print()

//    println(flHostname + "  " + flPort)
//    val flumeStream = FlumeUtils.createPollingStream(ssc, flHostname, flPort, storageLevel)
//    flumeStream.foreachRDD( rdd => {
//      //rdd.count().map( cnt => "Received " + cnt + " flume events." ).print()
//      print(rdd.count().toString())
//      val count1 = MongoDBObject("count" -> rdd.count())
//      mysca.insert(count1)
//    })

    ssc.start()
    //计算完毕退出
    ssc.awaitTermination()
  }
}
