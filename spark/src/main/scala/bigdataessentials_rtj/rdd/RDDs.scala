package org.spark
package bigdataessentials_rtj.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SaveMode, SparkSession}

import scala.io.Source

object RDDs extends App {

  private val spark: SparkSession = SparkSession.builder()
    .appName("RDDs")
    .master("local")
    .getOrCreate()

  val sc = spark.sparkContext

  // 1 - parallelize an existing collection
  val numbers = 1 to 1000000
  val numbersRDD = sc.parallelize(numbers)

  // 2 - reading from files
  case class StockValue(symbol: String, date: String, price: Double)
  def readStocks(filename: String) = {
    val fileSource = Source.fromFile(filename)
    val stocks = fileSource
      .getLines()
      .drop(1)
      .map(line => line.split(","))
      .map(tokens => StockValue(tokens(0), tokens(1), tokens(2).toDouble))
      .toList
    fileSource.close()
    stocks
  }

  val stocksRDD = sc.parallelize(readStocks("spark/src/main/resources/data/stocks.csv"))
  stocksRDD

  // 2b - reading from files
  val stocksRDD2 = sc.textFile("spark/src/main/resources/data/stocks.csv")
    .map(line => line.split(","))
    .filter(tokens => tokens(0).toUpperCase() == tokens(0))
    .map(tokens => StockValue(tokens(0), tokens(1), tokens(2).toDouble))

  // 3 - read from a DF
  val stocksDf = spark.read
    .option("header", "true")
    .option("sep", ",")
    .option("inferSchema", "true")
    .csv("spark/src/main/resources/data/stocks.csv")

  import spark.implicits._
  val stocksDs = stocksDf.as[StockValue]
  private val stocksRDD3: RDD[StockValue] = stocksDs.rdd

  // RDD -> DF
  val numbersDf = numbersRDD.toDF("numbers") // lose the type info

  // RDD -> DS
  val numbersDs = spark.createDataset(numbersRDD) // keeps the type info

  // Transformations

  // counting
  val msftRDD = stocksRDD.filter(_.symbol == "MSFT") // lazy transformations
  private val msCount: Long = msftRDD.count() // eager ACTION

  // distinct
  val companyNamesRDD = stocksRDD.map(_.symbol).distinct() // also lazy

  // min and max
  implicit val stockOrdering: Ordering[StockValue] = Ordering.fromLessThan((sa, sb) => sa.price < sb.price)
  val minMsft = msftRDD.min() // action

  // reduce
  numbersRDD.reduce(_ + _)

  // grouping
  val groupedStocksRDD = stocksRDD.groupBy(_.symbol)
  // ^^ very expensive

  // Partitioning

  val repartitionedStocksRDD = stocksRDD.repartition(30)
  repartitionedStocksRDD.toDF.write
    .mode(SaveMode.Overwrite)
    .parquet("spark/src/main/resources/data/stocks30")

  /*
    Repartitioning is Expensive. Involves Shuffling
    Best practice: partition EARLY, then process that
    Size of a partition should be 10-100MB
   */

  // coalesce
  val coalescedRDD = repartitionedStocksRDD.coalesce(15)
  coalescedRDD.toDF.write
    .mode(SaveMode.Overwrite)
    .parquet("spark/src/main/resources/data/stocks15")

}
