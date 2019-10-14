package com.ingestpipeline.service;

import java.nio.charset.Charset;
import java.util.Date;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ingestpipeline.model.IncomingData;
import com.ingestpipeline.repository.ElasticSearchRepository;
import com.ingestpipeline.util.Constants;

/**
 * This is a Service Implementation for all the actions which are with respect to Elastic Search 
 * @author Darshan Nagesh
 *
 */
@Service(Constants.Qualifiers.ENRICHMENT_SERVICE)
public class EnrichmentServiceImpl implements EnrichmentService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(EnrichmentServiceImpl.class);
	private static final String SEPARATOR = "_"; 
	private static final String JSON_EXTENSION = ".json"; 
	private static final String OBJECTIVE = "enrichment";
    private final String indexServiceHost;
	private final String userName;
	private final String password;
	private final String elasticSearchIndexName;
	private final String elasticSearchDocumentType; 

	private static final String AUTHORIZATION = "Authorization";
	private static final String US_ASCII = "US-ASCII";
	private static final String BASIC_AUTH = "Basic %s";
	
	@Autowired
	private ElasticSearchRepository elasticRepository;
	
	public EnrichmentServiceImpl(
			@Value("${services.esindexer.host}") String indexServiceHost,
			@Value("${services.esindexer.username}") String userName,
			@Value("${services.esindexer.password}") String password,
			@Value("${es.index.name}") String elasticSearchIndexName, 
			@Value("${es.document.type}") String elasticSearchDocumentType) {
		this.indexServiceHost = indexServiceHost;
		this.userName = userName;
		this.password = password;
		this.elasticSearchIndexName = elasticSearchIndexName; 
		this.elasticSearchDocumentType = elasticSearchDocumentType;
	}
	
	@Override
	public Boolean enrichData(IncomingData incomingData) {
		// Read the config schema for Enrichment here
		/*JSONObject jsonSchema = new JSONObject(
				new JSONTokener(ClassLoader.getSystemClassLoader().getResourceAsStream(OBJECTIVE.concat(SEPARATOR)
						.concat(incomingData.getDataContext().concat(SEPARATOR).concat(incomingData.getDataContextVersion()).concat(JSON_EXTENSION)))));
		JSONObject jsonSubject = new JSONObject(incomingData.getDataObject());
		*/
		// Make the configured calls here to denormalize and push to Elastic Search

		pushToElasticSearchIndex(incomingData.getDataObject());
		return Boolean.TRUE;
	}
	
	private Boolean pushToElasticSearchIndex(Object object) { 
		Long currentDateTime = new Date().getTime(); 
		String url = String.format("%s%s/%s/%s", this.indexServiceHost, elasticSearchIndexName, elasticSearchDocumentType,currentDateTime);
		HttpHeaders headers = getHttpHeaders();
		LOGGER.info("Data Object to be added to ES : " + object);
		LOGGER.info("URL to invoke : " + url);
		elasticRepository.saveMyDataObject(object, url, headers);
		return Boolean.TRUE; 
	}
	
	/**
	 * A helper method to create the headers for Rest Connection with UserName
	 * and Password
	 * 
	 * @return HttpHeaders
	 */
	private HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(AUTHORIZATION, getBase64Value(userName, password));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	/**
	 * Helper Method to create the Base64Value for headers
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	private String getBase64Value(String userName, String password) {
		String authString = String.format("%s:%s", userName, password);
		byte[] encodedAuthString = Base64.encodeBase64(authString.getBytes(Charset.forName(US_ASCII)));
		return String.format(BASIC_AUTH, new String(encodedAuthString));
	}
	
	
	
	
}
 