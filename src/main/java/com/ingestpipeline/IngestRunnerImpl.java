package com.ingestpipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ingestpipeline.util.JobConfig;

@Component
@Order(1)
public class IngestRunnerImpl implements ApplicationRunner {

	public static final Logger LOGGER = LoggerFactory.getLogger(IngestRunnerImpl.class);
	
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		JobConfig.init();
	}
}
