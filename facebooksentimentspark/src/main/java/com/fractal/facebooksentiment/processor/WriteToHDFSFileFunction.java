package com.fractal.facebooksentiment.processor;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;

import scala.Tuple2;


public class WriteToHDFSFileFunction  implements Function<JavaRDD<Tuple2<Integer, String>>,
Void> {

private static final long serialVersionUID = 42l;


@Override
public Void call(JavaRDD<Tuple2<Integer, String>> rdd) throws Exception {
	if (rdd.count() <= 0) return null;
    /*String path = Properties.getString("rts.spark.hdfs_output_file") +
                  "_" +
                  time.milliseconds();
    rdd.saveAsTextFile(path);*/
    return null;
}
}