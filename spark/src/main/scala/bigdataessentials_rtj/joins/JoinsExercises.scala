package org.spark
package bigdataessentials_rtj.joins

import org.apache.spark.sql.functions.{col, max}
import org.apache.spark.sql.{DataFrame, DataFrameReader, SparkSession, functions}

object JoinsExercises extends App {

  private val spark: SparkSession = SparkSession.builder()
    .appName("Joins Exercises")
    .master("local")
    .getOrCreate()

  private val postgresReader: DataFrameReader = spark.read
    .format("jdbc")
    .option("driver", "org.postgresql.Driver")
    .option("url", "jdbc:postgresql://172.30.157.3:5432/rtjvm")
    .option("user", "docker")
    .option("password", "docker")

  private val employees: DataFrame = postgresReader.option("dbtable", "public.employees").load()
  private val salaries: DataFrame = postgresReader.option("dbtable", "public.salaries").load()
  private val deptManager: DataFrame = postgresReader.option("dbtable", "public.dept_manager").load()
  private val titles: DataFrame = postgresReader.option("dbtable", "public.titles").load()

  employees.show()
  salaries.show()

  employees.join(salaries, "emp_no").groupBy(salaries.col("emp_no")).max("salary").show()
  employees.join(deptManager, "emp_no", "left_anti").show()

  val mostRecentJobTitlesDf = titles.groupBy("emp_no", "title").agg(max("to_date"))
  val bestPaidEmployeesDf = employees.join(salaries.groupBy("emp_no").agg(max("salary").as("maxSalary")), "emp_no")
    .orderBy(col("maxSalary").desc).limit(10)
  val bestPaidJobsDf = bestPaidEmployeesDf.join(mostRecentJobTitlesDf, "emp_no")

  bestPaidJobsDf.show()

}
