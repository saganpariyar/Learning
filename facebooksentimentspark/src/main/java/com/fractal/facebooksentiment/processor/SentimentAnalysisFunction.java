package com.fractal.facebooksentiment.processor;

import org.apache.spark.api.java.function.Function;

import scala.Tuple2;

public class SentimentAnalysisFunction implements Function<Tuple2<String, String>, Tuple2<Integer, String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Tuple2<Integer, String> call(Tuple2<String, String> postCommentTuple)
			throws Exception {
		
		return FacebookSentimentCalculator.getSentimentValueForPost(postCommentTuple);
	}

}
