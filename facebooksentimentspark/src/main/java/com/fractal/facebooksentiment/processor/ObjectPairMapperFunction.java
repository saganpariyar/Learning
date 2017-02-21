package com.fractal.facebooksentiment.processor;

import org.apache.spark.api.java.function.Function;

import scala.Tuple2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fractal.facebooksentiment.model.FacebookVo;

/*public  final class ObjectPairMapperFunction implements
PairFunction<Tuple2<String, String>, String, String> {
*//**
* 
*//*
private static final long serialVersionUID = 1L;
private final ObjectMapper objectMapper;

public ObjectPairMapperFunction(ObjectMapper objectMapper) {
this.objectMapper = objectMapper;
}

@Override
public Tuple2<String, String> call(Tuple2<String, String> v1)
	throws Exception {
FacebookVo facebookVo = objectMapper.readValue(v1._2, FacebookVo.class);
Tuple2<String, String> tuple = new Tuple2<String, String>(facebookVo.getKey(), facebookVo.getValue());
return tuple;
}
}*/

public  final class ObjectPairMapperFunction 
implements Function<Tuple2<String, String>, Tuple2<String, String>>{
/**
* 
*/
private static final long serialVersionUID = 1L;
private final ObjectMapper objectMapper;

public ObjectPairMapperFunction(ObjectMapper objectMapper) {
this.objectMapper = objectMapper;
}

@Override
public Tuple2<String, String> call(Tuple2<String, String> tupleOfKeyValue)
	throws Exception {
FacebookVo facebookVo = objectMapper.readValue(tupleOfKeyValue._2, FacebookVo.class);
System.out.println("here"+facebookVo.getKey()+" " + facebookVo.getValue());
Tuple2<String, String> tuple = new Tuple2<String, String>(facebookVo.getKey(), facebookVo.getValue());
return tuple;
}
}