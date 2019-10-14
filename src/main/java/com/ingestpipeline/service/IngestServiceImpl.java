package com.ingestpipeline.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ingestpipeline.model.IncomingData;
import com.ingestpipeline.producer.IngestProducer;
import com.ingestpipeline.util.ApplicationProperties;
import com.ingestpipeline.util.Constants;

/**
 * This is a Service Implementation for all the actions which are with respect to Elastic Search 
 * @author Darshan Nagesh
 *
 */
@Service(Constants.Qualifiers.INGEST_SERVICE)
public class IngestServiceImpl implements IngestService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(IngestServiceImpl.class);

	@Autowired
	private IngestProducer ingestProducer;
	
	@Autowired
	private ApplicationProperties applicationProperties; 
	
	@Override
	public Boolean ingestToPipeline(IncomingData incomingData) {
		LOGGER.info("Fetching the Incoming Data Config for the data received");
		String topic = "";
		String key = "";
		try { 
			if (applicationProperties.getPipelinerules().get(Constants.PipelineRules.VALIDATE_DATA)) {
				topic = applicationProperties.getTransactionIngestTopic();
				key = applicationProperties.getTransactionIngestKey();
			} else if (applicationProperties.getPipelinerules().get(Constants.PipelineRules.TRANSFORM_DATA)) {
				topic = applicationProperties.getTransactionTransformationTopic();
				key = applicationProperties.getTransactionTransformationKey();
			} else if (applicationProperties.getPipelinerules().get(Constants.PipelineRules.ENRICH_DATA)) {
				topic = applicationProperties.getTransactionEnrichmentTopic();
				key = applicationProperties.getTransactionEnrichmentKey();
			}
		} catch (Exception e) { 
			LOGGER.error("Encountered an Exception while Pushing the Data to pipeline on Ingest Service " + e.getMessage());
			ingestProducer.pushToPipeline(incomingData, Constants.KafkaTopics.ERROR_INTENT, Constants.KafkaTopics.ERROR_INTENT);
		}
		
		ingestProducer.pushToPipeline(incomingData, topic, key);
		return true;
	}
}
 