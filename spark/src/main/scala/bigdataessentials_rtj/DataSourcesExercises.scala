package org.spark
package bigdataessentials_rtj

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
 * Read the movies DF, then write it as:
 *  - tab-separated values file
 *  - snappy Parquet
 *  - table in the "public.movies" Postgres DB (docker is needed)
 */
object DataSourcesExercises extends App {

  val spark = SparkSession.builder()
    .appName("Data sources exercises")
    .config("spark.master", "local")
    .getOrCreate()

  private val moviesDF: DataFrame = spark.read.json("spark/src/main/resources/data/movies.json")

  moviesDF.write
    .mode(SaveMode.Overwrite)
    .option("header", "true")
    .option("sep", "\t")
    .csv("spark/src/main/resources/data/exercises/movies-duplicate.csv")

  moviesDF.write
    .mode(SaveMode.Overwrite)
    .option("compression", "snappy")
    .parquet("spark/src/main/resources/data/exercises/movies-duplicate.parquet")

  // OR
//  moviesDF.write.save("spark/src/main/resources/data/exercises/movies-duplicate.parquet")

  // Docker with Postgres is required
//  moviesDF.write
//    .format("jdbc")
//    .option("driver", "org.postgresql.Driver")
//    .option("url", "jdbc:postgresql://localhost:5432/rtjvm")
//    .option("user", "docker")
//    .option("password", "docker")
//    .option("dbtable", "public.employees")
//    .save()

}
