package org.spark
package bigdataessentials_rtj.managingnulls

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object ManagingNulls extends App {

  private val spark: SparkSession = SparkSession.builder()
    .appName("Common Spark Types")
    .master("local")
    .getOrCreate()

  val moviesDf = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/movies.json")

  // Select the first non-null value
  moviesDf.select(
    col("Title"),
    coalesce(col("Rotten_Tomatoes_Rating"), col("IMDB_Rating") * 10)
  )

  // Checking for nulls
  moviesDf.select("*").where(col("Rotten_Tomatoes_Rating").isNull)

  // Nulls when ordering
  moviesDf.orderBy(col("IMDB_Rating").desc_nulls_last)

  // Removing nulls
  moviesDf.select("Title", "IMDB_Rating").na.drop

  // Replace nulls
  moviesDf.na.fill(0, List("IMDB_Rating", "Rotten_Tomatoes_Rating"))
  moviesDf.na.fill(Map(
    "IMDB_Rating" -> 0,
    "Rotten_Tomatoes_Rating" -> 0
  ))

  // Complex operations
  moviesDf.selectExpr(
    "Title",
    "IMDB_Rating",
    "Rotten_Tomatoes_Rating",
    "ifnull(Rotten_Tomatoes_Rating, IMDB_Rating * 10) as ifnull",
    "nvl(Rotten_Tomatoes_Rating, IMDB_Rating * 10) as nvl",
    "nullif(Rotten_Tomatoes_Rating, IMDB_Rating * 10) as nullif",
    "nvl2(Rotten_Tomatoes_Rating, IMDB_Rating * 10, 0.0) as nvl2"
  )

}
