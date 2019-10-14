package com.ingestpipeline.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingestpipeline.model.IncomingData;
import com.ingestpipeline.producer.IngestProducer;
import com.ingestpipeline.service.ValidationService;
import com.ingestpipeline.util.ApplicationProperties;
import com.ingestpipeline.util.Constants;

@Service
public class ValidatorConsumer implements KafkaConsumer {

	public static final Logger LOGGER = LoggerFactory.getLogger(ValidatorConsumer.class);
	
	@Autowired
	private ValidationService validationService;

	@Autowired
	private IngestProducer ingestProducer;
	
	@Autowired
	private ApplicationProperties applicationProperties; 
    
    @KafkaListener(topics = {Constants.KafkaTopics.INGEST_DATA} , containerFactory = Constants.BeanContainerFactory.INCOMING_KAFKA_LISTENER)
    public void processMessage(IncomingData consumerRecord,
			@Header(KafkaHeaders.RECEIVED_TOPIC) final String topic) {
    	LOGGER.info("Validator Consumer");
    	LOGGER.info("Message Received at : " + topic);
    	LOGGER.info("Incoming Data : " + consumerRecord.getDataContext() + " :: " + consumerRecord.getDataContextVersion() + " :: " + consumerRecord.getDataObject());
    	LOGGER.info("##KafkaMessageAlert## : key:" + topic + ":" + "value:" + consumerRecord);
    	try {
			boolean isValid = validationService.validateData(consumerRecord);
			String nextTopic = ""; 
			String nextKey = "";
			if (isValid) {
				if(applicationProperties.getPipelinerules().get(Constants.PipelineRules.TRANSFORM_DATA)) { 
					nextTopic = applicationProperties.getTransactionValidationTopic();
					nextKey = applicationProperties.getTransactionValidationKey();
				} else  if(applicationProperties.getPipelinerules().get(Constants.PipelineRules.ENRICH_DATA)) { 
					nextTopic = applicationProperties.getTransactionTransformationTopic();
					nextKey = applicationProperties.getTransactionTransformationKey();
				}
				ingestProducer.pushToPipeline(consumerRecord, nextTopic, nextKey);
			} else {
				ingestProducer.pushToPipeline(consumerRecord, Constants.KafkaTopics.ERROR_INTENT, Constants.KafkaTopics.ERROR_INTENT);
			}
			LOGGER.info("Next Topic: " + nextTopic);
			LOGGER.info("Next Key: " + nextKey);

		} catch (final Exception e) {
			LOGGER.error("Exception Encountered while processing the received message : " + e.getMessage());
		}
    	
		
	}

}
