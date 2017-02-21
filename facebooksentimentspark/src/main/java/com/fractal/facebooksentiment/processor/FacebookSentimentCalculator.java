/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fractal.facebooksentiment.processor;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.SortedMap;

import org.codehaus.jettison.json.JSONException;

import scala.Tuple2;

import com.fractal.facebooksentiment.model.Constants;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;

/**
 *
 * @author sagan
 */
public class FacebookSentimentCalculator implements Serializable {

	private static final long serialVersionUID = -1589237412352702183L;

	private SortedMap<String, Integer> afinnSentimentMap = null;
	private SortedMap<String, Integer> commentSentimentMap = null;
	private static FacebookSentimentCalculator _singleton;

	private FacebookSentimentCalculator() {
		commentSentimentMap = Maps.newTreeMap();
		afinnSentimentMap = Maps.newTreeMap();

		try {
			final URL url = Resources.getResource(Constants.AFINN_SENTIMENT_FILE_NAME);
			final String text = Resources.toString(url, Charsets.UTF_8);
			final Iterable<String> lineSplit = Splitter.on("\n").trimResults().omitEmptyStrings().split(text);
			List<String> tabSplit;
			for (final String str : lineSplit) {
				tabSplit = Lists.newArrayList(Splitter.on("\t").trimResults().omitEmptyStrings().split(str));
				afinnSentimentMap.put(tabSplit.get(0), Integer.parseInt(tabSplit.get(1)));
			}
		} catch (final IOException ioException) {
			ioException.printStackTrace();
			// Should not occur. If it occurs, we cant continue. So, exiting at this point itself.
			// System.exit(1);
		}
	}

	private static FacebookSentimentCalculator get() {
		if (_singleton == null) {
			_singleton = new FacebookSentimentCalculator();
		}
		return _singleton;
	}

	public static Tuple2<Integer, String> getSentimentValueForPost(Tuple2<String, String> postCommentTuple) throws JSONException {

		FacebookSentimentCalculator fc = get();
		final int sentimentOfCurrentFbComment = fc.getSentimentOfFacebook(postCommentTuple._2());
		Integer previousSentiment = fc.commentSentimentMap.get(postCommentTuple._1());
		previousSentiment = null == previousSentiment ? sentimentOfCurrentFbComment : previousSentiment + sentimentOfCurrentFbComment;
		fc.commentSentimentMap.put(postCommentTuple._1(), previousSentiment);
		return new Tuple2<Integer, String>(previousSentiment, postCommentTuple._1());
	}

	private int getSentimentOfFacebook(final String value) {
		// Remove all punctuation and new line chars in the tweet.
		final String comment = value.replaceAll("\\p{Punct}|\\n", " ").toLowerCase();
		// Splitting the tweet on empty space.
		final Iterable<String> words = Splitter.on(' ').trimResults().omitEmptyStrings().split(comment);
		int sentimentOfCurrentFbComment = 0;
		for (String word : words) {
			if (afinnSentimentMap.containsKey(word)) {
				sentimentOfCurrentFbComment += afinnSentimentMap.get(word);
			}
		}
		return sentimentOfCurrentFbComment;
	}
}
