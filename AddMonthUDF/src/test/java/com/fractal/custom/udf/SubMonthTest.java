package com.fractal.custom.udf;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.apache.hadoop.hive.ql.udf.generic.GenericUDF.DeferredJavaObject;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF.DeferredObject;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.junit.Test;

public class SubMonthTest {
    private final String TEST_DATA = "2014-01-01";
    private final SubMonth subMonth = new SubMonth();
    ObjectInspector ob = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
    ObjectInspector ob1 = PrimitiveObjectInspectorFactory.javaIntObjectInspector;
    private final ObjectInspector[]arg = { ob, ob1 };

    @Test
    public void testAdd1Month() throws Exception {
        DeferredJavaObject def1 = new DeferredJavaObject(TEST_DATA);
        DeferredJavaObject def2 = new DeferredJavaObject(1);

        subMonth.initialize(arg);

        DeferredObject[] def = { def1, def2 };
        String resultData = subMonth.evaluate(def).toString();
        assertThat(resultData, is("2013-12-01"));
    }

    @Test
    public void testAdd12Month() throws Exception {
        DeferredJavaObject def1 = new DeferredJavaObject(TEST_DATA);
        DeferredJavaObject def2 = new DeferredJavaObject(12);

        subMonth.initialize(arg);

        DeferredObject[] def = { def1, def2 };
        String resultData = subMonth.evaluate(def).toString();
        assertThat(resultData, is("2013-01-01"));
    }

    @Test
    public void testSub1Month() throws Exception {
        DeferredJavaObject def1 = new DeferredJavaObject(TEST_DATA);
        DeferredJavaObject def2 = new DeferredJavaObject(-1);

        subMonth.initialize(arg);

        DeferredObject[] def = { def1, def2 };
        String resultData = subMonth.evaluate(def).toString();
        assertThat(resultData, is("2014-02-01"));
    }

    @Test
    public void testSub12Month() throws Exception {
        DeferredJavaObject def1 = new DeferredJavaObject("2015-01-12");
        DeferredJavaObject def2 = new DeferredJavaObject(2);

        subMonth.initialize(arg);

        DeferredObject[] def = { def1, def2 };
        String resultData = subMonth.evaluate(def).toString();
        assertThat(resultData, is("2014-11-01"));
    }

    @Test
    public void incorrectInput() throws Exception {
        DeferredJavaObject def1 = new DeferredJavaObject("InvalidString");
        DeferredJavaObject def2 = new DeferredJavaObject(12);

        subMonth.initialize(arg);

        DeferredObject[] def = { def1, def2 };
        Object resultData = subMonth.evaluate(def);
        assertNull(resultData);
    }
}
