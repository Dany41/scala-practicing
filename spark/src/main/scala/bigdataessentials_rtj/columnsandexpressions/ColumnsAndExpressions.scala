package org.spark
package bigdataessentials_rtj.columnsandexpressions

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.{col, column, expr}

object ColumnsAndExpressions extends App {

  val spark = SparkSession.builder()
    .appName("DF Columns and Expressions")
    .config("spark.master", "local")
    .getOrCreate()

  val carsDF = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/cars.json")

  // Columns
  val firstColumn = carsDF.col("Name")

  // Selecting (or projecting, for example below - one column projected into new DF with one column)
  val carNamesDF = carsDF.select(firstColumn)

  // Various select methods
  import spark.implicits._
  carsDF.select(
    carsDF.col("Name"),
    col("Acceleration"),
    column("Weight_in_lbs"),
    'Year, // Scala Symbol, auto-converted to column
    $"Horsepower", // fancier interpolated string, returns a Column object
    expr("Origin") // EXPRESSION
  )

  // Select with plain column names
  carsDF.select("Name", "Year")

  // EXPRESSIONS
  val simplestExpression = carsDF.col("Weight_in_lbs") // Column object is subtype of the Expression
  val weightInKgExpression = carsDF.col("Weight_in_lbs") / 2.2

  val carsWithWeightsDF = carsDF.select(
    col("Name"),
    col("Weight_in_lbs"),
    weightInKgExpression.as("Weight_in_kg"),
    expr("Weight_in_lbs / 2.2").as("Weight_in_kg_2")
  )

  // selectExpr
  val carsWithSelectExprWeightsDF = carsDF.selectExpr(
    "Name",
    "Weight_in_lbs",
    "Weight_in_lbs / 2.2",
  )

  // DF processing

  // adding a column
  val carsWithKg3DF: DataFrame = carsDF.withColumn("Weight_in_kg_3", col("Weight_in_lbs") / 2.2)
  // renaming a column
  val carsWithColumnRenamed = carsDF.withColumnRenamed("Weight_in_lbs", "Weight in pounds")
  // careful with column names
  carsWithColumnRenamed.selectExpr("`Weight in pounds`") // use ` to escape special symbols, like space in this example
  // remove a column
  carsWithColumnRenamed.drop("Cylinders", "Displacement")

  // filtering
  val europeanCarsDF = carsDF.filter(col("Origin") =!= "USA")
  val europeanCarsDF2 = carsDF.where(col("Origin") =!= "USA")
  // filtering with expression strings
  val americanCarsDF = carsDF.filter("Origin = 'USA'")
  // chain filters
  val americanPowerfulCarsDF = carsDF.filter(col("Origin") === "USA").filter(col("Horsepower") > 150)
  val americanPowerfulCarsDF2 = carsDF.filter(col("Origin") === "USA" and col("Horsepower") > 150)
  val americanCarsDF3 = carsDF.filter("Origin = 'USA' and Horsepower > 150")

  // unioning = adding more rows
  val moreCarsDF = spark.read.option("inferSchema", "true").json("spark/src/main/resources/data/more_cars.json")
  val allCarsDF = carsDF.union(moreCarsDF) // works if the DFs have the same schema

  // distinct values
  val allCountriesDF = carsDF.select("Origin").distinct()

  allCountriesDF.show()

}
