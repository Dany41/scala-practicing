package org.spark
package bigdataessentials_rtj

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types.{DateType, DoubleType, IntegerType, StringType, StructField, StructType}

/**
 * Read another file from the data/ folder, e.g. movies.json\
 *  - print its schema
 *  - count the number of rows, call count()
 */
object ReadJsonFileExercise extends App {

  val spark = SparkSession.builder()
    .appName("Read JSON File Exercise")
    .config("spark.master", "local")
    .getOrCreate()

//  val moviesSchema = StructType(Array(
//    StructField("Title", StringType),
//    StructField("US_Gross", IntegerType),
//    StructField("Worldwide_Gross", IntegerType),
//    StructField("US_DVD_Sales", IntegerType),
//    StructField("Production_Budget", IntegerType),
//    StructField("Release_Date", StringType),
//    StructField("MPAA_Rating", StringType),
//    StructField("Running_Time_min", IntegerType),
//    StructField("Distributor", StringType),
//    StructField("Source", StringType),
//    StructField("Major_Genre", StringType),
//    StructField("Creative_Type", StringType),
//    StructField("Director", StringType),
//    StructField("Rotten_Tomatoes_Rating", IntegerType),
//    StructField("IMDB_Rating", DoubleType),
//    StructField("IMDB_Votes", IntegerType)
//  ))

  private val movies: DataFrame = spark.read
    .format("json")
    //    .schema(moviesSchema)
    .option("inferSchema", "true")
    .load("spark/src/main/resources/data/movies.json")

  movies.printSchema()

  println(s"Movies coung: ${movies.count()}")

}
