package com.ingestpipeline.util;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

public class ResponseGenerator {


	private static ObjectMapper objectMapper = new ObjectMapper();

    public static String failureResponse() throws JsonProcessingException{
	ObjectNode response = objectMapper.createObjectNode();
	response.put(JsonKey.STATUS_CODE, ResponseCode.FAILURE.getErrorCode());
	response.put(JsonKey.STATUS_MESSAGE,
		ResponseCode.FAILURE.getErrorMessage());
	response.put(JsonKey.ERROR_MESSAGE,
		ResponseCode.FAILURE.getErrorMessage());
	return JSONObjectUtil.getJsonString(objectMapper,response);
    }
    
    
    public static String failureResponse(String message) throws JsonProcessingException{
        ObjectNode actualResponse = objectMapper.createObjectNode();

    	ObjectNode response = objectMapper.createObjectNode();
    	response.put(JsonKey.STATUS_CODE, ResponseCode.FAILURE.getErrorCode());
    	response.put(JsonKey.STATUS_MESSAGE,
    		ResponseCode.FAILURE.getErrorMessage());
    	response.put(JsonKey.ERROR_MESSAGE,message);
        actualResponse.putPOJO(JsonKey.STATUS,response);

    	return JSONObjectUtil.getJsonString(objectMapper,actualResponse);
        }

    public static String feedbackFailureResponse(String message) throws JsonProcessingException{
        ObjectNode actualResponse = objectMapper.createObjectNode();

    	ObjectNode response = objectMapper.createObjectNode();
    	response.put(JsonKey.STATUS_CODE, ResponseCode.FAILURE.getErrorCode());
    	response.put(JsonKey.STATUS_MESSAGE,
    		ResponseCode.FAILURE.getErrorMessage());
    	response.put(JsonKey.ERROR_MESSAGE,message);
        actualResponse.putPOJO(JsonKey.STATUS,response);

    	return JSONObjectUtil.getJsonString(objectMapper,actualResponse);
        }

	public static String successResponse(Object obj) throws JsonProcessingException {
		ObjectNode actualResponse = objectMapper.createObjectNode();

		ObjectNode response = objectMapper.createObjectNode();
		response.put(JsonKey.STATUS_CODE, ResponseCode.Success.getErrorCode());
		response.put(JsonKey.STATUS_MESSAGE, ResponseCode.Success.getErrorMessage());
		response.put(JsonKey.ERROR_MESSAGE, "");
		actualResponse.putPOJO(JsonKey.STATUS,response);
		if (obj != null) {
			actualResponse.putPOJO(JsonKey.RESPONSE_DATA, obj);
		}

		return JSONObjectUtil.getJsonString(objectMapper,actualResponse);
	}
	
	public static String feedbackSuccessResponse(Object obj) throws JsonProcessingException {
		ObjectNode response = objectMapper.createObjectNode();
		response.put(JsonKey.STATUS_CODE, ResponseCode.Success.getErrorCode());
		response.put(JsonKey.STATUS_MESSAGE, ResponseCode.Success.getErrorMessage());
		response.put(JsonKey.ERROR_MESSAGE, "");
		return JSONObjectUtil.getJsonString(objectMapper,response);
	}

	/**
     * this method will crate success response and send to controller.
     *
     * @return ObjectNode object.
     */
	public static String feedbackSuccessResponse(String message) throws JsonProcessingException {
		ObjectNode response = objectMapper.createObjectNode();
		response.put(JsonKey.STATUS_CODE, ResponseCode.Success.getErrorCode());
		response.put(JsonKey.STATUS_MESSAGE, ResponseCode.Success.getErrorMessage());
		response.put(JsonKey.ERROR_MESSAGE, "");
		return JSONObjectUtil.getJsonString(objectMapper, response);
	}
}
