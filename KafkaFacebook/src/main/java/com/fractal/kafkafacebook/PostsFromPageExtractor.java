/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fractal.kafkafacebook;

/**
 *
 * @author karan.gusani
 */
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.PagableList;
import facebook4j.Paging;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

public class PostsFromPageExtractor {

	/**
	 * A simple Facebook4J client which illustrates how to access group feeds /
	 * posts / comments.
	 *
	 * @param args
	 * @throws FacebookException
	 */
	private static ObjectWriter ow;
	private static ObjectMapper objectMapper;

	public static String convertJavaObjectToJsonUsingJackson(Object object)
			throws JsonProcessingException {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
			ow = objectMapper.writer();
		}
		return ow.writeValueAsString(object);
	}

	public static void main(String[] args) throws FacebookException,
			IOException, Exception {

		Context context = new Context(
				"C://sagan//fractal//works//coding//Facebook//kafkafacebook//KafkaFacebook//src//main//resources//facebook.properties");
		Properties props = new Properties();

		props.put("metadata.broker.list",
				context.getString(Constants.BROKER_LIST));
		props.put("serializer.class", context.getString(Constants.SERIALIZER));
		props.put("request.required.acks",
				context.getString(Constants.REQUIRED_ACKS));

		ProducerConfig config = new ProducerConfig(props);
		final Producer<String, String> producer = new Producer<>(config);

		// ConfigurationBuilder confBuilder = new ConfigurationBuilder();
		// confBuilder.setDebugEnabled(true);
		// confBuilder.setOAuthAppId("933228633389469");
		// confBuilder.setOAuthAppSecret("5377a79db23c27ce71b997cca6a892c2");
		// confBuilder.setUseSSL(true);
		// confBuilder.setJSONStoreEnabled(true);
		//
		// Configuration configuration = confBuilder.build();
		// Generate facebook instance.
		Facebook facebook = new FacebookFactory().getInstance();

		// Use default values for oauth app id.
		facebook.setOAuthAppId("1006884539378801",
				"0bb18cd52889431efd8333df4307abfc");
		// Get an access token from:
		// https://developers.facebook.com/tools/explorer
		// Copy and paste it below.
		String accessTokenString = context
				.getString(Constants.ACCESS_TOKEN_KEY);
		AccessToken at = new AccessToken(accessTokenString);

		// Set access token.
		facebook.setOAuthAccessToken(at);

		// final String facebookDate = new SimpleDateFormat().format(new
		// Date());
		// __paging_token=enc_AdDo3sDBkWJMtTLZAZAb3vjOQTNWNvCUbuquocLVEFMPYjnqnauInfuZAjl3sx2R2mJJT924Wjxmmr4jgwSSP6PHhrV
		Reading r = new Reading();
		// r.until("2014-08-15");
		// r.since("2012-08-20");

		r.limit(100);
		// r.withLocation();

		// We're done.
		// Access group feeds.
		// You can get the group ID from:
		// https://developers.facebook.com/tools/explorer
		// Set limit to 25 feeds.
		ResponseList<Post> feeds = facebook.getFeed(
				context.getString(Constants.KEYWORDS_KEY), r);
		System.out.println(facebook.getPage(context.getString(Constants.KEYWORDS_KEY)).getLikes());
		System.out.println(facebook.getPage(context.getString(Constants.KEYWORDS_KEY)).getName());
		System.out.println(facebook.getPage(context.getString(Constants.KEYWORDS_KEY)).getWereHereCount());
		System.out.println(facebook.getPage(context.getString(Constants.KEYWORDS_KEY)).isCommunityPage());
		System.out.println(facebook.getPage(context.getString(Constants.KEYWORDS_KEY)).isPublished());
		System.out.println(facebook.getPage(context.getString(Constants.KEYWORDS_KEY)).getAbout());
		// System.out.println(feeds.size());
		int count = 0;
		

		for (Post post : feeds) {
			String message = post.getMessage();
			String id = post.getId();
			System.out.println("Id "+id+" message : "+message);
			System.out.println("Shares "+post.getSharesCount());
			System.out.println("Name "+post.getName());
			System.out.println("Likes "+post.getStatusType());
			System.out.println("Likes "+post.getProperties());
			try {
				PagableList<Comment> comments = post.getComments();
				System.out.println(comments.size());
				Paging<Comment> paging;
				do {
					for (Comment comment : comments) {
						//System.out.println("Comments:::::" + comment.getFrom().getName());
						//System.out.println("Comments:::::" + comment.getFrom().getId());
						//System.out.println("Comments:::::" + comment.getLikeCount());
						System.out.println("Comments:::::" + facebook.getLocations(comment.getFrom().getId()));
						//System.out.println("Comments:::::" + facebook.getLocations());
						//System.out.println("Comments:::::" + facebook.getAccounts().get(0).getName());
						FacebookVo facebookVo = new FacebookVo();
						//facebookVo.setKey(message);
						//facebookVo.setValue(comment.getMessage());

						KeyedMessage<String, String> data = new KeyedMessage<>(
								context.getString(Constants.KAFKA_TOPIC),
								convertJavaObjectToJsonUsingJackson(facebookVo));
						/*System.out.println("data :::" + data);*/
						producer.send(data);
						/*System.out.println("Success ::" + comment.getMessage());*/
					}
					paging = comments.getPaging();
				} while ((paging != null)
						&& ((comments = facebook.fetchNext(paging)) != null));
				// System.out.println("Comments count ::::" + count++);
			} catch (FacebookException ex) {
				Logger.getLogger(PostsFromPageExtractor.class.getName()).log(
						Level.SEVERE, null, ex);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
