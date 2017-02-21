/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fractal.kafkafacebook;

/**
 *
 * @author saumya.goyal
 */
public class Constants {

    public static final String FB_APP_KEY = "FB_APP_KEY";
    public static final String FB_SECRET_KEY = "FB_SECRET_KEY";
    public static final String ACCESS_TOKEN_KEY = "FB_ACCESS_TOKEN";

    public static final String BATCH_SIZE_KEY = "batchSize";
    public static final long DEFAULT_BATCH_SIZE = 1000L;
    public static final String KEYWORDS_KEY = "FB_KEYWORDS";

    public static final String BROKER_LIST = "broker.list";
    public static final String SERIALIZER = "serializer.class";
    public static final String REQUIRED_ACKS = "request.required.acks";
    public static final String KAFKA_TOPIC = "kafka.topic";

}
