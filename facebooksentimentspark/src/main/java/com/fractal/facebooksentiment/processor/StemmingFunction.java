package com.fractal.facebooksentiment.processor;

import java.io.Serializable;
import java.util.StringTokenizer;

import org.apache.spark.api.java.function.Function;
import org.tartarus.snowball.ext.EnglishStemmer;

import scala.Tuple2;

public class StemmingFunction extends EnglishStemmer
    implements Function<Tuple2<String, String>, Tuple2<String, String>>, Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


    @Override
    public Tuple2<String, String> call(Tuple2<String, String> CommentsOnPost)
    {
        String text = CommentsOnPost._2();
        text = stemmingCurrentComment(text);
        return new Tuple2<String, String>(CommentsOnPost._1(), text);
    }

	private String stemmingCurrentComment(String text) {
		StringBuilder sb=new StringBuilder();
		StringTokenizer tok =new StringTokenizer(text);
		String word;
	    while (tok.hasMoreTokens()) {
	      word= tok.nextToken();
	      this.setCurrent(word);
	      this.stem();
	      word = this.getCurrent();
	      sb.append(" " + word);
	    }
		return sb.toString().trim();
	}
}
