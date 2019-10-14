package com.ingestpipeline.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.everit.json.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.bazaarvoice.jolt.JsonUtils;

@PropertySource(value= {"classpath:application.properties"})
public class JobConfig {
	
	private static final String SEPARATOR = "_";
	private static final String JSON_EXTENSION = ".json";
	private static final String CONFIGROOT = "config/";
	private static Map<String, Schema> schemaCache = new HashMap<>();
	public static volatile Map<String, List<Object>> transformationSchemaMap = new ConcurrentHashMap<>();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JobConfig.class);

    public static void init() {
    	// getTransformationConfigLocations();
    }
    
    public static void getTransformationConfigLocations() { 
		/*// Fetching the Validation Configuration for the associated use cases
    	String transformConfigLocations = ApplicationProperties.getTransformationConfigLocations(); 
    	if(StringUtils.isNotBlank(transformConfigLocations)) { 
    		String[] locations = transformConfigLocations.split(","); 
    		for(String loc : locations) { 
    			List<Object> schemaList = JsonUtils.classpathToList(loc); 
    			transformationSchemaMap.put(loc, schemaList); 
    		}
    	}*/
    	
    	
    	List<Object> schemaList = null; /*JsonUtils.jsonToList(this.getClass().getClassLoader().getResourceAsStream("config/transform_transaction_v1.json"));*/
    	transformationSchemaMap.put("transform_transaction_v1.json", schemaList); 
	}
	
	public static List<Object> getSchemaForConfigLocation(String location) { 
		return transformationSchemaMap.get(location); 
	}

    


}
