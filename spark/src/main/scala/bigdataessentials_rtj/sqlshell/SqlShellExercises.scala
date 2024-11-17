package org.spark
package bigdataessentials_rtj.sqlshell

import org.apache.spark.sql.SparkSession

object SqlShellExercises extends App {

  private val spark: SparkSession = SparkSession.builder()
    .appName("Scala Shell")
    .master("local")
    .config("spark.sql.warehouse.dir", "spark/src/main/resources/warehouse")
    .getOrCreate()

  spark.sql("create database rtjvm")
  spark.sql("use rtjvm")

//  val moviesDf = spark.read
//    .option("inferSchema", "true")
//    .json("spark/src/main/resources/data/movies.json")
//
//  moviesDf.createOrReplaceTempView("movies")
//  moviesDf.write
//    .mode(SaveMode.Overwrite)
//    .saveAsTable("movies")

  spark.sql("show databases").show()
//  spark.read.table("employees")
//  spark.read.table("salaries")
//  spark.read.table("departments")

  spark.sql(
    """
      |select count(e.*) as cnt from employees e where e.hire_date between '1999-01-01' and '2001-01-01'
      |""".stripMargin
  ).show

  spark.sql(
    """
      |select de.dept_no, avg(s.salary) as avg
      | from employees e, salaries s, dept_emp de
      | where 1=1
      |   and e.emp_no=s.emp_no
      |   and e.emp_no=de.emp_no
      |   and e.hire_date between '1999-01-01' and '2001-01-01'
      | group by de.dept_no
      |""".stripMargin
  ).show

  spark.sql(
    """
      |select d.dept_name, avg(s.salary) as avg
      | from employees e, salaries s, dept_emp de, departments d
      | where 1=1
      |   and e.emp_no=s.emp_no
      |   and e.emp_no=de.emp_no
      |   and de.dept_no=d.dept_no
      |   and e.hire_date between '1999-01-01' and '2001-01-01'
      | group by d.dept_name
      | order_by avg desc
      | limit 1
      |""".stripMargin
  ).show

}
