package org.spark
package bigdataessentials_rtj.rdd

import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.io.Source

object RDDsExercises extends App {

  private val spark: SparkSession = SparkSession.builder()
    .appName("RDDs")
    .master("local")
    .getOrCreate()

  val sc = spark.sparkContext

  case class Movie(title: String, genre: String, rating: Double)

  private val moviesDf: DataFrame = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/movies.json")

  import spark.implicits._
  val moviesRDD = moviesDf
    .select(col("Title").as("title"), col("Major_Genre").as("genre"), col("IMDB_Rating").as("rating"))
    .where(col("genre").isNotNull and col("rating").isNotNull)
    .as[Movie]
    .rdd

  moviesRDD.map(_.genre).distinct().toDF.show

  moviesRDD.filter(movie => movie.genre == "Drama" && movie.rating > 6).toDF.show

  case class GenreAvgRating(genre: String, rating: Double)
  moviesRDD.groupBy(_.genre).map { case (genre, movies) => GenreAvgRating(genre, movies.map(_.rating).sum/movies.size) }.toDF.show

}
