package org.spark
package bigdataessentials_rtj.datasets

import org.apache.spark.sql.{Dataset, Encoders, SparkSession}

object Datasets extends App {

  private val spark: SparkSession = SparkSession.builder()
    .appName("Common Spark Types")
    .master("local")
    .getOrCreate()

  val numbersDf = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("spark/src/main/resources/data/numbers.csv")

  // Convert a DF to a Dataset
  implicit val intEncoder = Encoders.scalaInt
  val numbersDs: Dataset[Int] = numbersDf.as[Int]

  // Dataset of a complex type
  case class Car(
                Name: String,
                Miles_per_Gallon: Option[Double],
                Cylinders: Long,
                Displacement: Double,
                Horsepower: Long,
                Weight_in_lbs: Long,
                Acceleration: Double,
                Year: String,
                Origin: String
                )

  val carsDf = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/cars.json")

  import spark.implicits._
  val carsDs = carsDf.as[Car]

  // DS collection functions
  numbersDs.filter(_ < 100).show

  // Joins

  case class Guitar(id: Long, make: String, model: String, guitarType: String)
  case class GuitarPlayer(id: Long, name: String, guitars: Seq[Long], band: Long)
  case class Band(id: Long, name: String, homeTown: String, year: Long)

  val guitarsDs = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/guitars.json").as[Guitar]

  val guitarPlayersDs = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/guitarPlayers.json").as[GuitarPlayer]

  val bandsDs = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/bands.json").as[Band]

  val guitarPlayerBandsDs = guitarPlayersDs.joinWith(bandsDs, guitarPlayersDs.col("band") === bandsDs.col("id"), "inner")

  guitarPlayerBandsDs.show

  // Grouping DS
  val carsGroupedByOrigin = carsDs
    .groupByKey(_.Origin)
    .count()

  carsGroupedByOrigin.show
}
