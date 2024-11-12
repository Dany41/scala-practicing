package org.spark
package bigdataessentials_rtj.joins

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.expr

object Joins extends App {

  private val spark: SparkSession = SparkSession.builder()
    .appName("Joins")
    .master("local")
    .getOrCreate()

  val guitarsDf = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/guitars.json")

  val guitaristsDf = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/guitarPlayers.json")

  val bandsDf = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/bands.json")

  // joins

  private val joinConditions = guitaristsDf.col("band") === bandsDf.col("id")
  val guitaristsBandsDf = guitaristsDf.join(bandsDf, joinConditions, "inner")
  guitaristsBandsDf.show()

  // outer joins
  // left outer
  guitaristsDf.join(bandsDf, joinConditions, "left_outer").show()
  // right outer
  guitaristsDf.join(bandsDf, joinConditions, "right_outer").show()
  // full outer
  guitaristsDf.join(bandsDf, joinConditions, "outer").show()
  // semi-joins
  guitaristsDf.join(bandsDf, joinConditions, "left_semi").show()
  // anti-joins
  guitaristsDf.join(bandsDf, joinConditions, "left_anti").show()

  // things to bear in mind
  guitaristsDf.join(bandsDf.withColumnRenamed("id", "band"), "band")
  guitaristsBandsDf.drop(bandsDf.col("id"))
  val bandsModDf = bandsDf.withColumnRenamed("id", "bandId")
  guitaristsDf.join(bandsDf, bandsModDf.col("band") === bandsDf.col("bandId"), "inner")

  // using complex types
  guitaristsDf.join(guitarsDf.withColumnRenamed("id", "guitarId"), expr("array_contains(guitars, guitarId)"))

}
