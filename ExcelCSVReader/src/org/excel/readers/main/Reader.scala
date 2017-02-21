package org.excel.readers.main
import org.apache.spark.SparkConf

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.excel.readers.main.CSVReader
import org.excel.readers.main.ExcelReader

object Reader {
  def main(args: Array[String]) {
     val conf = new SparkConf().setAppName("SparkProgram").setMaster("local")
     val sc = new SparkContext(conf)
     val sqlContext = new SQLContext(sc)
     //import sqlContext.implicits._

     
     // Create an RDD
val people = sc.textFile("C:/sagan/fractal/My Workspaces/TrainingWKS/ExcelCSVReader/words.csv")

// The schema is encoded in a string
val schemaString = "name:String age:Double loc:String"

// Import Row.
import org.apache.spark.sql.Row;

// Import Spark SQL data types
import org.apache.spark.sql.types.{StructType,StructField,StringType, DoubleType,LongType};

// Generate the schema based on the string of schema
val schema =
  StructType(
    schemaString.split(" ").map( fieldName => if(fieldName.split(":")(1)=="String")StructField(fieldName.split(":")(0), StringType, true) 
        else if(fieldName.split(":")(1)=="Double") StructField(fieldName.split(":")(0), DoubleType, true) else StructField(fieldName.split(":")(0), LongType, true)))

// Convert records of the RDD (people) to Rows.
val rowRDD = people.map(_.split(",")).map(p => Row(p(0), p(1).trim))

// Apply the schema to the RDD.
val peopleDataFrame = sqlContext.createDataFrame(rowRDD, schema)

// Register the DataFrames as a table.
peopleDataFrame.registerTempTable("people")

// SQL statements can be run by using the sql methods provided by sqlContext.
val results = sqlContext.sql("SELECT name FROM people")

results.show()
// The results of SQL queries are DataFrames and support all the normal RDD operations.
// The columns of a row in the result can be accessed by field index or by field name.
results.map(t => "Name: " + t(0)).collect().foreach(println)
     
     
     
     
     
     
     
     
     
     
     
     // val csvReader = new CSVReader()
     ///csvReader.read(sqlContext,args(0))
     
     
    //val excelReader = new ExcelReader()
    //excelReader.read(sqlContext,args(0))
     
    /* case class Test(id:String,filed2:String)
val myFile = conext.textFile("file.txt").toDF()
myFile.show()*/
//val df= myFile.map( x => x.split(";") ).map( x=> Test(x(0),x(1)) )
     
  }
}