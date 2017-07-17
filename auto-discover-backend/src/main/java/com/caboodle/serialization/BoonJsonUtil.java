package com.caboodle.serialization;

import java.util.ArrayList;
import java.util.List;

import io.advantageous.boon.json.JsonFactory;
import io.advantageous.boon.json.ObjectMapper;
import io.vertx.core.json.JsonObject;

/**
 * @author harishchauhan
 *
 */
public class BoonJsonUtil {

	public static String serialize(Object objectData) {
		String a = null;
		try{
		ObjectMapper mapper = JsonFactory.createUseAnnotations(false);
		a = mapper.writeValueAsString(objectData);
		}catch(Exception e){
			
		}
		return a;
	}
	
	public static <T> JsonObject serializeList(List<T> modelList) {
		JsonObject jsonObject = new JsonObject();
		int counter = 0;
		try{
			for (T model : modelList) {
				jsonObject.put(++counter+"", serialize(model));
			}
		
		}catch(Exception e){
			
		}
		return jsonObject;
	}
	
	

	public static <T> T deSerialize(String serializedData, Class<T> clazz) {
		ObjectMapper mapper = JsonFactory.createUseAnnotations(false);
		T n = mapper.fromJson(serializedData, clazz);
		return n;
	}

	public static <T> List<T> deSerializeJsonObjectList(List<JsonObject> jsonObjectList, Class<T> clazz) {
		List<T> resultList = new ArrayList<>();
		for (JsonObject jsonObject : jsonObjectList) {
			resultList.add(BoonJsonUtil.deSerialize(jsonObject.toString(), clazz));
		}
		return resultList;
	}
}
