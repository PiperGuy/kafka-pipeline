package com.ingestpipeline.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.ingestpipeline.model.IncomingData;
import com.ingestpipeline.producer.IngestProducer;
import com.ingestpipeline.service.TransformService;
import com.ingestpipeline.util.ApplicationProperties;
import com.ingestpipeline.util.Constants;

@Service
public class TransformConsumer implements KafkaConsumer {

	public static final Logger LOGGER = LoggerFactory.getLogger(TransformConsumer.class);

	@Autowired
	private TransformService transformService;

	@Autowired
	private IngestProducer ingestProducer;
	
	@Autowired
	private ApplicationProperties applicationProperties; 

	@Override
	@KafkaListener(topics = { Constants.KafkaTopics.VALID_DATA }, containerFactory = Constants.BeanContainerFactory.INCOMING_KAFKA_LISTENER)
	public void processMessage(IncomingData incomingData,  
			@Header(KafkaHeaders.RECEIVED_TOPIC) final String topic) {
		LOGGER.info("##KafkaMessageAlert## : key:" + topic + ":" + "value:" + incomingData);
		try {
			boolean isTransformed = transformService.transformData(incomingData);
			if (isTransformed) {
				ingestProducer.pushToPipeline(incomingData, applicationProperties.getTransactionTransformationTopic(), applicationProperties.getTransactionTransformationKey());
			} else {
				ingestProducer.pushToPipeline(incomingData, Constants.KafkaTopics.ERROR_INTENT, Constants.KafkaTopics.ERROR_INTENT);
			}
		} catch (final Exception e) {
			LOGGER.error("Exception Encountered while processing the received message : " + e.getMessage());
		}
	}
}
