package org.excel.readers.main
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.Row
import java.io.File
import java.io.FileWriter
import java.io.IOException

 class CSVReader {
  def read(context:SQLContext, path:String){
    println("CSVReader")  
    var df = context.read.format("com.databricks.spark.csv").option("header", "true").load(path)
		//var df = context.read.option("header", "true").load(path)
    df.show()
		df.registerTempTable("temp")
		var df1 = context.sql("select name, count*23 as count from temp")
		df1.show()
		writeIntoExcel("output.csv", df1.collect())
  }
  
  
  def writeIntoExcel(file:String, rows:Array[Row]) {
		println("hihssssssssssssssssssssssssssssssssssssssssssssss")
		val file1 = new File(file)
		try {

			var fileWriter = new FileWriter(file1)
			fileWriter.write("name,count\n")
			
			for(row <- rows) fileWriter.write(row.getString(0) + "," + row.getString(1) + "\n")

			fileWriter.close()
		} catch  {
			case ex: IOException => {
            println("IO Exception")
         }
		}
	}
}