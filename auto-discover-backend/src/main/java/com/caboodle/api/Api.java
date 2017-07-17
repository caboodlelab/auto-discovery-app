package com.caboodle.api;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caboodle.annotation.UrlPath;
import com.caboodle.enums.Result;
import com.caboodle.exception.AbstractException;
import com.caboodle.route.AbstractJsonHandler;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@Service
@UrlPath(name = "/fetch-data", produces = "text/html", method = "GET")
public class Api extends AbstractJsonHandler<JsonObject> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Api.class);
	
	@Inject
	private ApiUtility uility;
	
	@Override
	protected void performTask(JsonObject request) throws AbstractException {

		try {
			RoutingContext routingContext = getRoutingContext();
			
			uility.perform().setHandler(res->{
				if(res.succeeded()){
					routingContext.response().end(res.result());
				}else{
					routingContext.response().end(res.cause().getMessage());
				}
			});
			
		} catch (Exception e) {
			LOGGER.error("Error occured while getting details", e);
			JsonObject jsonObject = new JsonObject();
			jsonObject.put("error_message", Result.INTERNAL_ERROR.getResultMessage());
			jsonObject.put("error_code", Result.INTERNAL_ERROR.getResultCode());
			getRoutingContext().response().end(jsonObject.toString());
		}
	}
	
	@Override
	protected Object readBufferedRequest(Buffer buffer) {
		JsonObject jsonObject = new JsonObject();
		return super.readBufferedRequest(buffer.appendString(jsonObject.toString()));
	}
}
