package com.caboodle.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caboodle.util.ConfigManager;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
 * @author harishchauhan
 *
 */
public enum EMongo {
	

	INSTANCE;
	
	private MongoClient client;
	private final static Logger LOGGER = LoggerFactory.getLogger(EMongo.class);
	private final Properties property = ConfigManager.INSTANCE.getAppConfig().getProperties();
	
	private List<ServerAddress> getMongoServerConfigs(String connectionString){
		List<ServerAddress> serverList = new ArrayList<>();
		String [] nodeInfoArray = connectionString.trim().split(",");
		for (String node : nodeInfoArray) {
			try{
			String[] nodeAddressAry = node.trim().split(":");
			serverList.add(new ServerAddress(nodeAddressAry[0],Integer.parseInt(nodeAddressAry[1])));
			}catch(Exception e){
				LOGGER.error("Exception while mongo host init.");
			}
		}
		return serverList;
	}
	
	private EMongo() {
		
		 client = new MongoClient(getMongoServerConfigs(property.getProperty("db.mongo.connectionString")));
	}
	
	public MongoClient getClient() {
		return client;
	}
	

}
