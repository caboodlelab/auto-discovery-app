package com.caboodle.route;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.caboodle.annotation.UrlPath;
import com.caboodle.exception.AbstractException;
import com.caboodle.exception.ApplicationException;
import com.caboodle.exception.ValidationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.buffer.Buffer;


/**
 * @author harishchauhan
 *
 * @param <R>
 */
public abstract class AbstractJsonHandler<R> extends AbstractHttpHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJsonHandler.class);

	@Override
	protected Object readBufferedRequest(Buffer buffer) throws AbstractException {
		Object readValue = null;
		ObjectMapper mapper = new ObjectMapper();
		try {

			Pattern pattern = Pattern.compile("([A-Za-z]|\\.)+");
			Matcher matcher = pattern.matcher(getClass().getGenericSuperclass().getTypeName());

			matcher.find();
			matcher.find(); // We want to read the request class specified by <R, T>
			readValue = mapper.readValue(buffer.getBytes(), Class.forName(matcher.group()));

		} catch (JsonParseException | JsonMappingException e) {
			LOGGER.error("Error parsing request to JSON ", e.getMessage());
			throw new ValidationException("Bad request format");
		} catch (IOException e) {
			LOGGER.error("Error reading stream", e);
			throw new ApplicationException("Error reading stream");
		} catch (ClassNotFoundException e) {
			LOGGER.error("Error reading buffer", e);
			throw new ApplicationException("Error reading buffer");
		}
		return readValue;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void handleRequest(Object r) throws AbstractException {
		R r1 = (R) r;
		performTask(r1);
	}

	protected abstract void performTask(R request) throws AbstractException;

	@Override
	protected Map<String, String> getHeaders() {
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("content-type", getClass().getAnnotation(UrlPath.class).produces());
		hashMap.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS,DELETE");
		hashMap.put("Access-Control-Allow-Headers", "Authorization,Origin,X-Requested-With,Content-Type,Accept,merchantAccessKey,requestSignature");
		hashMap.put("Access-Control-Allow-Origin", "*");
		hashMap.put("Access-Control-Max-Age", "3600");
		return hashMap;
	}
}
