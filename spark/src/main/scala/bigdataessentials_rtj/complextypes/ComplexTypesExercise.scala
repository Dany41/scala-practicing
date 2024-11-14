package org.spark
package bigdataessentials_rtj.complextypes

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object ComplexTypesExercise extends App {

  private val spark: SparkSession = SparkSession.builder()
    .appName("Common Spark Types")
    .master("local")
    .getOrCreate()

  val stocksDf = spark.read
    .option("header", "true")
    .option("sep", ",")
    .option("inferSchema", "true")
    .csv("spark/src/main/resources/data/stocks.csv")

  stocksDf.select(col("symbol"), to_date(col("date"), "MMM d yyyy"), col("price")).show

}
