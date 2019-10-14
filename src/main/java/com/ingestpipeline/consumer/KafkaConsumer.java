package com.ingestpipeline.consumer;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import com.ingestpipeline.model.IncomingData;

public interface KafkaConsumer {
	
	public void processMessage(final IncomingData incomingData,
			@Header(KafkaHeaders.RECEIVED_TOPIC) final String topic); 

}
