package org.spark
package bigdataessentials_rtj.aggregations

import org.apache.spark.sql.functions.{avg, col, countDistinct, expr, mean, stddev, sum}
import org.apache.spark.sql.{DataFrame, SparkSession}

object AggregationsExercises extends App {

  private val spark: SparkSession = SparkSession.builder()
    .appName("Aggregations Exercises")
    .master("local")
    .getOrCreate()

  private val moviesDf: DataFrame = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/movies.json")

  moviesDf.select(expr("sum(Worldwide_Gross - Production_Budget)").as("Profit_Sum")).show()
  moviesDf.select(countDistinct(col("Director"))).show()
  moviesDf.select(
    mean(col("US_Gross")),
    stddev(col("US_Gross"))
  ).show()
  moviesDf.select("Director", "US_Gross", "IMDB_Rating")
    .groupBy("Director")
    .agg(
      avg(col("US_Gross")).as("US_Gross_Avg"),
      avg(col("IMDB_Rating")).as("IMDB_Rating_Avg")
    )
    .orderBy(col("IMDB_Rating_Avg").desc_nulls_last)
    .show()

}
