package com.ingestpipeline.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ingestpipeline.model.IncomingData;
import com.ingestpipeline.service.IngestService;
import com.ingestpipeline.util.Constants;

@RestController
@RequestMapping(Constants.Paths.ELASTIC_PUSH_CONTROLLER_PATH)
public class RestApiController {

	public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	IngestService ingestService; 

	/**
	 * This API receives the Transaction Details JSON Request and passes it on to the Service Layer for
	 * further process of persisting into elastic search database
	 * @param transaction
	 * @return
	 */
	@RequestMapping(value = Constants.Paths.SAVE, method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody IncomingData incomingData) {
		logMyTime();
		Boolean status = ingestService.ingestToPipeline(incomingData);
		if(status) { 
			return new ResponseEntity<String>(HttpStatus.CREATED);
		}
		return new ResponseEntity<String>(HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	private void logMyTime() {
		logger.info("System Time is : " + new Date());
		SimpleDateFormat sd = new SimpleDateFormat(Constants.DATE_FORMAT);
		Date date = new Date();
		sd.setTimeZone(TimeZone.getTimeZone(Constants.INDIAN_TIMEZONE));
		logger.info("Time at timezone IST : " + sd.format(date));
	}
}