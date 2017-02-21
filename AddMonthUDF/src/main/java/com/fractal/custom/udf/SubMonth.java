package com.fractal.custom.udf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.DateObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.IntObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

public class SubMonth extends GenericUDF {

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        if (arguments.length != 2) {
            throw new UDFArgumentLengthException("The function sub_month(local_date, months_to_sub) requires 2 arguments.");
        }

        ObjectInspector localDateVal = arguments[0];
        ObjectInspector monthsToAddVal = arguments[1];

        if (!(localDateVal instanceof StringObjectInspector)) {
            throw new UDFArgumentException("First argument must be of type Date (local_date as String)");
        }
        if (!(monthsToAddVal instanceof IntObjectInspector)) {
            throw new UDFArgumentException("Second argument must be of type int (Month to add)");
        }
        return PrimitiveObjectInspectorFactory.writableStringObjectInspector;
    }

    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        String localDateVal = (String) ObjectInspectorUtils.copyToStandardJavaObject(arguments[0].get(),
                PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        /*IntWritable monthsToAddVal = (IntWritable) ObjectInspectorUtils.copyToStandardJavaObject(arguments[1].get(),
                PrimitiveObjectInspectorFactory.javaIntObjectInspector);*/
        IntWritable monthsToSubVal  =  new IntWritable(Integer.parseInt(arguments[1].get().toString()));

        LocalDate localDate = null;
        try {
            localDate = LocalDate.parse(localDateVal, DateTimeFormat.forPattern("yyyy-MM-dd"));
        } catch (Exception ex) {
            return null;
        }

        
        System.out.println(getFirstDayDate(localDate.plusMonths(-monthsToSubVal.get())));
        return new Text(getFirstDayDate(localDate.plusMonths(-monthsToSubVal.get())));
    }

    private String getFirstDayDate(LocalDate date) {
		String customDate = "";
		customDate = customDate + date.getYear()+"-";
		customDate = customDate + getProperNumber(customDate, date.getMonthOfYear())+"-";
		customDate = customDate + "01";
		
    	
    	System.out.println(customDate);
    	return customDate;
    	
	}

	private String getProperNumber(String customDate, int d) {
		if(d<10){
    		return  "0"+d; 
    	}
    	else{
    		return  ""+d;
    	}
		
	}

	@Override
    public String getDisplayString(String[] arguments) {
        assert (arguments.length == 2);
        return "add_month(" + arguments[0] + ", " + arguments[1] + ")";
    }
}
