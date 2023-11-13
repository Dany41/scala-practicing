package org.spark
package bigdataessentials_rtj

import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType}


/**
 * Create a manual DF describing smartphones:
 *  - made
 *  - model
 *  - screen dimension
 *  - camera megapixels
 */
object CreateManualDataFrameExercise extends App {

  val spark = SparkSession.builder()
    .appName("Create Manual DF Exercise")
    .config("spark.master", "local")
    .getOrCreate()

  val rows = List(
    ("Nokia", "b12", 2015, 5.5, 43.0),
    ("Xiaomi", "Redmi 8", 2019, 6.0, 24.0),
    ("iPhone", "15 Pro", 2023, 5.6, 83.0),
    ("Samsung", "a24", 2022, 6.6, 48.0),
  )

  import spark.implicits._
  val frame: DataFrame = rows.toDF("Made by", "Model", "Year", "Screen Dimension", "Camera Megapixels")

  frame.show()

}
