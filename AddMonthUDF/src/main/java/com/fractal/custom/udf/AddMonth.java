package com.fractal.custom.udf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.IntObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

public class AddMonth extends GenericUDF {

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        if (arguments.length != 2) {
            throw new UDFArgumentLengthException("The function add_month(local_date, months_to_add) requires 2 arguments.");
        }

        ObjectInspector localDateVal = arguments[0];
        ObjectInspector monthsToAddVal = arguments[1];

        if (!(localDateVal instanceof StringObjectInspector)) {
            throw new UDFArgumentException("First argument must be of type String (local_date as String)");
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
        IntWritable monthsToAddVal  =  new IntWritable(Integer.parseInt(arguments[1].get().toString()));

        LocalDate localDate = null;
        try {
            localDate = LocalDate.parse(localDateVal, DateTimeFormat.forPattern("yyyy-MM-dd"));
        } catch (Exception ex) {
            return null;
        }

        
        return new Text(localDate.plusMonths(monthsToAddVal.get()).toString());
    }

    @Override
    public String getDisplayString(String[] arguments) {
        assert (arguments.length == 2);
        return "add_month(" + arguments[0] + ", " + arguments[1] + ")";
    }
}
