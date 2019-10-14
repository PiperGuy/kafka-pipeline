package com.ingestpipeline.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import com.ingestpipeline.model.IncomingData;
import com.ingestpipeline.util.Constants;
import com.ingestpipeline.util.JobConfig;
	
/**
 * This is a Service Implementation for all the actions which are with respect to Elastic Search 
 * @author Darshan Nagesh
 *
 */
@Service(Constants.Qualifiers.TRANSFORM_SERVICE)
public class TransformServiceImpl implements TransformService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(TransformServiceImpl.class);
	private static final String SEPARATOR = "_"; 
	private static final String JSON_EXTENSION = ".json"; 

	@Override
	public Boolean transformData(IncomingData incomingData) {
		List chainrSpecJSON = JsonUtils.jsonToList(this.getClass().getClassLoader().getResourceAsStream("config/transform_transaction_v1.json"));
        Chainr chainr = Chainr.fromSpec( chainrSpecJSON );
        Object inputJSON = incomingData.getDataObject(); 
        try { 
            Object transformedOutput = chainr.transform( inputJSON );
            incomingData.setDataObject(transformedOutput);
            return Boolean.TRUE; 
        } catch (Exception e) { 
        	LOGGER.error("Encountered an error while tranforming the JSON : " + e.getMessage());
        	return Boolean.FALSE; 
        }
	}
}
 