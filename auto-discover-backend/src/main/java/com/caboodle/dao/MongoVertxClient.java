package com.caboodle.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caboodle.runner.ApplicationMainRunner;
import com.caboodle.util.CommonUtil;
import com.caboodle.util.ConfigManager;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * @author harishchauhan
 *
 */
@Service
public class MongoVertxClient {
	
	private final Properties property = ConfigManager.INSTANCE.getAppConfig().getProperties();
	
	private final static Logger LOGGER = LoggerFactory.getLogger(MongoVertxClient.class);
	
	private List<JsonObject> getConnectionConfig(String connectionString){
		List<JsonObject> connectionJsonList = new ArrayList<JsonObject>();
		String [] nodeInfoArray = connectionString.trim().split(",");
		for (String node : nodeInfoArray) {
			try{
			JsonObject json = new JsonObject();
			String[] nodeAddressAry = node.trim().split(":");
			json.put("host", nodeAddressAry[0]);
			json.put("port", Integer.parseInt(nodeAddressAry[1]));
			connectionJsonList.add(json);
			}catch(Exception e){
				LOGGER.error("Exception while mongo host init.");
			}
		}
		return connectionJsonList;
		
	}
	
	private JsonArray getHostArray(String connectionString){
		JsonArray jsonArray = new JsonArray();
		for (JsonObject connectionJson : getConnectionConfig(connectionString)) {
			jsonArray.add(connectionJson);
		}
		return jsonArray;
	}
	
	public MongoClient createClient() {
		
		JsonObject mongoConfigJson = new JsonObject().put("hosts", getHostArray( property.getProperty("db.mongo.connectionString")));
				mongoConfigJson.put("db_name", property.getProperty("db.mongo.name"));
				if(CommonUtil.isNotEmpty(property.getProperty("db.mongo.replicaset.name")))
					mongoConfigJson.put("replicaSet",property.getProperty("db.mongo.replicaset.name"));
				if(CommonUtil.isNotEmpty(property.getProperty("db.mongo.maxPoolSize")))
					mongoConfigJson.put("maxPoolSize",Integer.parseInt(property.getProperty("db.mongo.maxPoolSize")));
				if(CommonUtil.isNotEmpty(property.getProperty("db.mongo.minPoolSize")))
					mongoConfigJson.put("minPoolSize",Integer.parseInt(property.getProperty("db.mongo.minPoolSize")));
				LOGGER.info(mongoConfigJson.toString());
				MongoClient client = MongoClient.createShared(ApplicationMainRunner.getVertxInstance(), mongoConfigJson);
		return client;		
	}
}

