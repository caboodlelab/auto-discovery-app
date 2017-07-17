package com.caboodle.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.glassfish.hk2.api.PostConstruct;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caboodle.entity.DBEntity;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * @author harishchauhan
 *
 */
@Service
public class GetDBModelUtility implements IGetDBModel,PostConstruct {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetDBModelUtility.class);

	@Inject
	private MongoVertxClient mongoVertxClient;

	private MongoClient client;

	@Override
	public void postConstruct() {
		client = this.mongoVertxClient.createClient();
	}
	

	public Future<List<DBEntity>> fetchByFind(Map<String, Object> params) {
		Future<List<DBEntity>> future = Future.future();
		LOGGER.info("inside db query impl");
		try {
			JsonObject query = new JsonObject();
			for (Entry<String, Object> entry : params.entrySet()) {
				query.put(entry.getKey(), entry.getValue());
			}
			
			client.find("employee",query , res ->{
				if(res.succeeded()){
					List<JsonObject> result = res.result();
					List<DBEntity> dbEntities = result.parallelStream().map(eachDbObj->{
						return getDBEntity(eachDbObj);
					}).collect(Collectors.toList());
					
					future.complete(dbEntities);
				}else{
					future.fail(res.cause());
				}
			});
			
		} catch (Exception e) {
			LOGGER.error("Error while fetching data", e);
			future.fail(new Exception("Error while fetching data"));
		}
		return future;
	}
	
	private DBEntity getDBEntity(JsonObject dbObject){
		if(null!=dbObject){
			DBEntity entity = new DBEntity();
			entity.setId(dbObject.getInteger("id"));
			entity.setName(dbObject.getString("name"));
			return entity;
		}
		return null;
	}


}
