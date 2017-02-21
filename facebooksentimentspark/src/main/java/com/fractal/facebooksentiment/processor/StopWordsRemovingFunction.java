package com.fractal.facebooksentiment.processor;

import java.util.List;

import org.apache.spark.api.java.function.Function;

import scala.Tuple2;

public class StopWordsRemovingFunction  implements Function<Tuple2<String, String>, Tuple2<String, String>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public Tuple2<String, String> call(Tuple2<String, String> tupleOfPostComment)
    {
        String comment = tupleOfPostComment._2();
        List<String> stopWords = StopWords.getWords();
        for (String word : stopWords)
        {
            comment = comment.replaceAll("\\b" + word + "\\b", "");
        }
        return new Tuple2<String, String>(tupleOfPostComment._1(), comment);
    }

}
