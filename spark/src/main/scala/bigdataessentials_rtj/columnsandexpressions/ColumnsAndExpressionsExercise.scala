package org.spark
package bigdataessentials_rtj.columnsandexpressions

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, expr}
import org.spark.bigdataessentials_rtj.columnsandexpressions.ColumnsAndExpressions.spark

/**
 * - Read Movies DF
 * - Select two arbitrary columns
 * - Create new DF by summing up the total profit columns
 * - Select all the comedy movies (Major_Genre) with IMDB rating above 6
 *
 * Use as many versions as possible
 */

object ColumnsAndExpressionsExercise extends App {

  val spark = SparkSession.builder()
    .appName("Columns And Expressions Exercises")
    .config("spark.master", "local")
    .getOrCreate()

  val moviesDF = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/movies.json")

  moviesDF.select(
    "Title",
    "Release_Date"
  ).show()

  moviesDF.select(
    col("Title"),
    expr("Release_Date")
  ).show()

  import spark.implicits._
  val moviesDFWithProfit = moviesDF.withColumn("Total Profit", expr("US_Gross + Worldwide_Gross - Production_Budget"))
  moviesDFWithProfit.select("Title", "Total Profit").show()

  val filteredMovies = moviesDF.where("IMDB_Rating > 6")
  filteredMovies.select("Title", "IMDB_Rating").show()

}
