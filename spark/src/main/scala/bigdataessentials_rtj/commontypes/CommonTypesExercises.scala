package org.spark
package bigdataessentials_rtj.commontypes

import org.apache.spark.sql.{Column, SparkSession}
import org.apache.spark.sql.functions.{col, regexp_extract}

object CommonTypesExercises extends App {

  private val spark: SparkSession = SparkSession.builder()
    .appName("Common Spark Types Exercises")
    .master("local")
    .getOrCreate()

  val carsDf = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/cars.json")

  val regexCarNames = getCarNames.map(_.toLowerCase).mkString("|")
  carsDf
    .select(col("Name"), regexp_extract(col("Name"), regexCarNames, 0).as("regex_match"))
    .where(col("regex_match") =!= "")
    .drop("regex_match")
    .show()

  private val filters: Column = getCarNames
    .map(_.toLowerCase)
    .map(col("Name").contains)
    .reduce(_ or _)
  carsDf.where(filters).show()

  def getCarNames: List[String] = List("Volkswagen", "Mercedes-Benz", "Ford")

}
