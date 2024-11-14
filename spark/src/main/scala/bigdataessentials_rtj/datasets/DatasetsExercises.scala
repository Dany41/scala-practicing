package org.spark
package bigdataessentials_rtj.datasets

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{array_contains, avg, col}

object DatasetsExercises extends App {

  private val spark: SparkSession = SparkSession.builder()
    .appName("Common Spark Types")
    .master("local")
    .getOrCreate()

  // Dataset of a complex type
  case class Car(
                  Name: String,
                  Miles_per_Gallon: Option[Double],
                  Cylinders: Long,
                  Displacement: Double,
                  Horsepower: Option[Long],
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

  println(carsDs.count())
  println(carsDs.filter(_.Horsepower.isDefined).filter(_.Horsepower.get > 140).count())
  println(carsDs.filter(_.Horsepower.isDefined).map(_.Horsepower.get).reduce(_ + _) / carsDs.filter(_.Horsepower.isDefined).count())

  carsDs.select(avg(col("Horsepower"))).show

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

  val guitarWithPlayersDs = guitarPlayersDs
    .joinWith(guitarsDs, array_contains(guitarPlayersDs.col("guitars"), guitarsDs.col("id")), "outer")

  guitarWithPlayersDs.show

}
