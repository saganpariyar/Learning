package com.fractal.facebooksentiment.controller;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.codehaus.jettison.json.JSONObject;

import scala.Tuple2;

public class JMSNofificationFunction  implements Function<JavaRDD<Tuple2<Integer, String>>,
Void> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Void call(JavaRDD<Tuple2<Integer, String>> rdd) throws Exception {
		rdd.foreach(new JMSSender());	
		return null;
	}

}

class JMSSender implements VoidFunction<Tuple2<Integer, String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void call(Tuple2<Integer, String> tupleForFacebookPostWithValue) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key", tupleForFacebookPostWithValue._2());
		jsonObject.put("value", tupleForFacebookPostWithValue._1());
		JmsMessageSender.send(jsonObject.toString());
	}
	
}
