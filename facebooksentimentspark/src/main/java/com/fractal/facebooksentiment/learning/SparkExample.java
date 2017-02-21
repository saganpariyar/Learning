package com.fractal.facebooksentiment.learning;

import java.io.IOException;
import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class SparkExample implements Serializable {
	transient private SparkConf sparkConf = new SparkConf().setAppName(
			"FacebookPostSentimentAnalysis").setMaster("local[2]");

	transient private JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

	transient private JavaStreamingContext sparkStreamingContext = new JavaStreamingContext(
			javaSparkContext, new Duration(1000));

	public static void main(String args[]) throws IOException {
		(new SparkExample()).startSparkStreaming();
	}

	public void startSparkStreaming() throws IOException {
		System.out.println("c://test//IFRToolLog.txt");

		JavaDStream<String> stream = sparkStreamingContext.textFileStream("c:\\test\\IFRToolLog.txt");
		
		JavaDStream<String> stream1  = stream.flatMap(new FlatMapFunction<String, String>() {
			 
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Iterable<String> call(String arg0) throws Exception {
				System.out.println("shihsi"+arg0);
				return null;
			}
		});
		
		stream1.print();

		sparkStreamingContext.start();
		sparkStreamingContext.awaitTermination();
	}

}
