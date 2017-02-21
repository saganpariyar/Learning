package com.fractal.facebooksentiment.sparktesting;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.hive.api.java.JavaHiveContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import scala.Tuple2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fractal.facebooksentiment.controller.JMSNofificationFunction;
import com.fractal.facebooksentiment.processor.ObjectPairMapperFunction;
import com.fractal.facebooksentiment.processor.SentimentAnalysisFunction;
import com.fractal.facebooksentiment.processor.StemmingFunction;
import com.fractal.facebooksentiment.processor.StopWordsRemovingFunction;

public class HiveSparkTesting {

	private SparkConf sparkConf = new SparkConf().setAppName("HiveSparkTesting")
			.setMaster("local[2]");
	JavaSQLContext sqlContext = new org.apache.spark.sql.api.java.JavaSQLContext(sparkConf);
	JavaHiveContext hiveContext = new org.apache.spark.sql.hive.api.java.HiveContext(sparkConf);
	private JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
	private JavaStreamingContext sparkStreamingContext = new JavaStreamingContext(javaSparkContext,
			new Duration(1000));

	public static void main(String args[]) {
		(new HiveSparkTesting()).startSparkStreaming();
	}

	public void startSparkStreaming() {

		final ObjectMapper objectMapper = new ObjectMapper();

		int numThreads = Integer.parseInt("2");
		Map<String, Integer> topicMap = new HashMap<String, Integer>();
		String[] topics = { "comments" };
		for (String topic : topics) {
			topicMap.put(topic, numThreads);
		}

		JavaPairReceiverInputDStream<String, String> streamFromKafka = KafkaUtils
				.createStream(sparkStreamingContext, "localhost:2181", "sparkStreaming",
						topicMap);
		//streamFromKafka.window(new Duration(10000), new Duration(2000));
		
		/*JavaPairDStream<String, String> tupleOfPostComment = streamFromKafka
				.mapToPair(new ObjectPairMapperFunction(objectMapper));*/
		
		JavaDStream<Tuple2<String, String>> tupleOfPostComment = streamFromKafka
				.map(new ObjectPairMapperFunction(objectMapper));
		
		JavaDStream<Tuple2<String, String>> filteredByStopWords = tupleOfPostComment
				.map(new StopWordsRemovingFunction());
		
		JavaDStream<Tuple2<String, String>> stemmedData = filteredByStopWords
				.map(new StemmingFunction());
		JavaDStream<Tuple2<Integer, String>> sentimentsData = stemmedData
				.map(new SentimentAnalysisFunction());
		sentimentsData.foreachRDD(new JMSNofificationFunction());
		
		// sentimentsData.foreachRDD(new WriteToHDFSFileFunction());
		// sentimentsData.print();
		sparkStreamingContext.start();
		sparkStreamingContext.awaitTermination();
	}

}