package org.spark
package bigdataessentials_rtj

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.types._

object DataSources extends App {

  val spark = SparkSession.builder()
    .appName("Data Sources and Formats")
    .config("spark.master", "local")
    .getOrCreate()

  val carsSchema = StructType(Array(
    StructField("Name", StringType),
    StructField("Miles_per_Gallon", DoubleType),
    StructField("Cylinders", LongType),
    StructField("Displacement", DoubleType),
    StructField("Horsepower", LongType),
    StructField("Weight_in_lbs", LongType),
    StructField("Acceleration", DoubleType),
    StructField("Year", DateType),
    StructField("Origin", StringType)
  ))

  /*
    Reading a DF:
      - format
      - schema (optional)
      - zero or more options
   */
  val carsDF: DataFrame = spark.read
    .format("json")
    .schema(carsSchema) // enforce a schema
    .option("mode", "failFast") // dropMalformed, permissive (default)
    .option("path", "spark/src/main/resources/data/cars.json")
    .option("dateFormat", "yyyy-MM-dd")
    .load()

  // alternative reading with options map
  val carsDFWithOptionMap = spark.read
    .format("json")
    .options(Map(
      "mode" -> "failFast",
      "path" -> "spark/src/main/resources/data/cars.json",
      "inferSchema" -> "true"
    ))
    .load()

  /*
   Writing DFs
    - format
    - save mode = overwrite, append, ignore, errorIfExists
    - path
    - zero or more options
   */
  carsDF.write
    .format("json")
    .mode(SaveMode.Overwrite)
    .save("spark/src/main/resources/data/cars_duplicate.json")

  // JSON flags
  spark.read
    .schema(carsSchema)
    .options(Map(
      "dateFormat" -> "yyyy-MM-dd", // couple with schema; if Spark fails parsing, it will put null
      "allowSingleQuotes" -> "true",
      "compression" -> "uncompressed" // default; others: bzip2, gzip, lz4, snappy, deflate
    ))
    .json("spark/src/main/resources/data/cars.json")

  // CSV flags

  val stocksSchema = StructType(Array(
    StructField("symbol", StringType),
    StructField("date", DateType),
    StructField("price", DoubleType)
  ))

  spark.read
    .schema(stocksSchema)
    .option("dateFormat", "MMM d yyyy")
    .option("header", "true") // csv specific option
    .option("sep", ",") // csv specific option; default - ','
    .option("nullValue", "")
    .csv("spark/src/main/resources/data/stocks.csv")

  // Parquet - open-sourced compressed binary data storage format;
  //            optimized for fast reading of columns;
  //            default storage format for data frames

  carsDF.write
    .mode(SaveMode.Overwrite)
    .parquet("spark/src/main/resources/data/cars.parquet")

  // Text files
  spark.read.text("spark/src/main/resources/data/sampleTextFile.txt").show()

  // Reading from a remote DB
  private val employeesDF: DataFrame = spark.read
    .format("jdbc")
    .option("driver", "org.postgresql.Driver")
    .option("url", "jdbc:postgresql://localhost:5432/rtjvm")
    .option("user", "docker")
    .option("password", "docker")
    .option("dbtable", "public.employees")
    .load()

  employeesDF.show(10)
}
