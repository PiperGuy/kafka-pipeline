package com.ingestpipeline.service;

import org.springframework.stereotype.Service;

import com.ingestpipeline.model.IncomingData;

@Service
public interface TransformService {
	
	Boolean transformData(IncomingData incomingData);

}
