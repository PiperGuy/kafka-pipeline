package com.ingestpipeline.util;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public class JSONObjectUtil {
	@Autowired
	public ObjectMapper mapper;
	@Autowired
	public Gson gson;


	/**
	 * @return
	 */
	public static String getJsonString(ObjectMapper objectMapper,Object object) throws JsonProcessingException {
		if(objectMapper != null){
			return  objectMapper.writeValueAsString(object);
		}
		return null;
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper){
		mapper=objectMapper;
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gsonn)
	{
		gson = gsonn;
	}
}
