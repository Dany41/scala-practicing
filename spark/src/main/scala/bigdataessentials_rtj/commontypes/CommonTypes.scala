package org.spark
package bigdataessentials_rtj.commontypes

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.{col, initcap, lit, not, regexp_extract, regexp_replace}

object CommonTypes extends App {

  private val spark: SparkSession = SparkSession.builder()
    .appName("Common Spark Types")
    .master("local")
    .getOrCreate()

  val moviesDf = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/movies.json")

  val carsDf = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/cars.json")

  // adding a plain value to a DF
  moviesDf.select(col("Title"), lit(47).as("plain_value")).show()

  // Booleans
  val dramaFilter = col("Major_Genre") equalTo "Drama"
  val goodRatingFilter = col("IMDB_Rating") > 7.0
  val preferredFilter = dramaFilter and goodRatingFilter

  moviesDf.select("title").where(preferredFilter)
  // + multiple ways of filtering
  val moviesWithGoodnessFlagsDf = moviesDf.select(col("Title"), preferredFilter.as("good_movie"))
  moviesWithGoodnessFlagsDf.where("good_movie")
  moviesWithGoodnessFlagsDf.where(not(col("good_movie")))

  // Numbers
  val moviesAvgRatingsDf = moviesDf.select(col("Title"), (col("Rotten_Tomatoes_Rating") / 10 + col("IMDB_Rating")) / 2)

  // correlation
  println(moviesDf.stat.corr("Rotten_Tomatoes_Rating", "IMDB_Rating")) // corr is an action

  // Strings
  carsDf.select(initcap(col("Name")))
  carsDf.select("*").where(col("Name").contains("volkswagen"))
  val regexString = "volkswagen|vw"
  private val vwDf: DataFrame = carsDf.select(
    col("Name"),
    regexp_extract(col("Name"), regexString, 0).as("regex_extract")
  ).where(col("regex_extract") =!= "").drop("regex_extract")

  vwDf.select(
    col("Name"),
    regexp_replace(col("Name"), regexString, "People's Car").as("regex_replace")
  ).show()



}
