package org.spark
package bigdataessentials_rtj.sqlshell

import org.apache.spark.sql.{DataFrame, DataFrameReader, SaveMode, SparkSession}
import org.apache.spark.sql.functions.col

object SqlShell extends App {

  private val spark: SparkSession = SparkSession.builder()
    .appName("Spark SQL Practice")
    .master("local")
    .config("spark.sql.warehouse.dir", "spark/src/main/resources/warehouse")
    .getOrCreate()

  val carsDf = spark.read
    .option("inferSchema", "true")
    .json("spark/src/main/resources/data/cars.json")

  // regular DF API
  carsDf.select(col("Name")).where(col("Origin") === "USA")

  // use Spark SQL
  carsDf.createOrReplaceTempView("cars")
  spark.sql(
    """
      |select Name from cars where Origin = 'USA'
      |"""
      .stripMargin)

  // we can run ANY SQL statement
  spark.sql("create database rtjvm")
  spark.sql("use rtjvm")
  val databasesDf = spark.sql("show databases")

  databasesDf.show

  private val postgresReader: DataFrameReader = spark.read
    .format("jdbc")
    .option("driver", "org.postgresql.Driver")
    .option("url", "jdbc:postgresql://172.30.157.3:5432/rtjvm")
    .option("user", "docker")
    .option("password", "docker")

  private val employees: DataFrame = postgresReader.option("dbtable", "public.employees").load()

  def transferTables(tableNames: List[String]) = tableNames.foreach { tableName =>
    val tableDf = postgresReader.option("dbtable", s"public.$tableName").load()
    tableDf.createOrReplaceTempView(tableName)
    tableDf.write
      .mode(SaveMode.Overwrite)
      .saveAsTable(tableName)
  }

  transferTables(List(
    "employees",
    "departments",
    "titles",
    "dept_emp",
    "salaries",
    "dept_manager")
  )

  // read DF from loaded tables
  val employeesDf2 = spark.read.table("employees")

}
