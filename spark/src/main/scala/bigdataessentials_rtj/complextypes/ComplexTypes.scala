package org.spark
package bigdataessentials_rtj.complextypes

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

object ComplexTypes extends App {

  private val spark: SparkSession = SparkSession.builder()
    .appName("Common Spark Types")
    .master("local")
    .getOrCreate()

  val moviesDf = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/movies.json")

  // Dates
  val moviesWithReleaseDates = moviesDf.select(col("Title"), to_date(col("Release_Date"), "dd-MMM-yy").as("Actual_Release"))

  moviesWithReleaseDates
    .withColumn("Today", current_date())
    .withColumn("Right_Now", current_timestamp())
    .withColumn("Movie_Age", datediff(col("Today"), col("Actual_Release")))

  // Structures
  moviesDf
    .select(col("Title"), struct(col("US_Gross"), col("Worldwide_Gross")).as("Profit"))
    .select(col("Title"), col("Profit").getField("US_Gross").as("US_Profit"))

  // With expression strings
  moviesDf
    .selectExpr("Title", "US_Gross, Worldwide_Gross as Profit")
    .selectExpr("Title", "Profit.US_Gross")

  // Arrays
  private val moviesWithWords: DataFrame = moviesDf.select(col("Title"), split(col("Title"), " |,").as("Title_Words"))

  moviesWithWords.select(
    col("Title"),
    expr("Title_Words[0]"),
    size(col("Title_Words")),
    array_contains(col("Title_Words"), "Love")
  ).show()

}
