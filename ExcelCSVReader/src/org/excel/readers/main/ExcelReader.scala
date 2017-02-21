package org.excel.readers.main

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.spark.sql.SQLContext
import org.excel.readers.main.Name



class ExcelReader {
  
  def read(context:SQLContext, path:String) {
    println("ExcelReader")
    var names = new ArrayList[Name]();
		try {
			val file = new FileInputStream(new File(path));

			// Create Workbook instance holding reference to .xlsx file
			var workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			var sheet = workbook.getSheetAt(0);
			
			// Iterate through each rows one by one
			var rowIterator = sheet.iterator();
			rowIterator.next();
			while (rowIterator.hasNext()) {
				var row = rowIterator.next();
				// For each row, iterate through all the columns
				var cellIterator = row.cellIterator();

				var name = new Name();
				while (cellIterator.hasNext()) {

					var cell = cellIterator.next();
					// Check the cell type and format accordingly
					cell.getCellType() match {
					case Cell.CELL_TYPE_NUMERIC => {
						print(cell.getNumericCellValue() + "t");
						name.setCount(cell.getNumericCellValue())
					}
					case Cell.CELL_TYPE_STRING =>{
						print(cell.getStringCellValue());
						name.setName(cell.getStringCellValue())
					}

				}
				names.add(name)

			}
			System.out.println(names)
			file.close()

		}
		}catch {
			case ex: Exception => {
            println("Exception")
         }
		}

		var df = context.createDataFrame(names, classOf[Name]);
		df.show();
		df.registerTempTable("temp");
		df = context.sql("select name, count*23 as count from temp");
		df.show();
		// df.withColumn("countNum", df.col("count")).write().format("com.databricks.spark.csv").save("words.csv");

		writeIntoExcel("output1.csv", df.collect)
 }
  
   
  def writeIntoExcel(file:String, rows:Array[org.apache.spark.sql.Row]) {
		println("hihssssssssssssssssssssssssssssssssssssssssssssss")
		val file1 = new File(file)
		try {

			var fileWriter = new FileWriter(file1)
			fileWriter.write("name,count\n")
			
			for(row <- rows) fileWriter.write(row.getString(0) + "," + row.getDouble(1) + "\n")

			fileWriter.close()
		} catch  {
			case ex: IOException => {
            println("IO Exception")
         }
		}
	}
   
}