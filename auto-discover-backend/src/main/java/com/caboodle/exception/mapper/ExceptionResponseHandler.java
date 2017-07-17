package com.caboodle.exception.mapper;

import java.io.StringWriter;
import java.util.function.Function;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.caboodle.bean.ResponseBean;
import com.caboodle.enums.ResponseType;
import com.caboodle.enums.Result;
import com.caboodle.exception.AbstractException;
import com.caboodle.exception.ApplicationException;
import com.caboodle.serialization.BoonJsonUtil;

import io.vertx.ext.web.RoutingContext;

/**
 * @author harishchauhan
 *
 */
public class ExceptionResponseHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionResponseHandler.class);

	public static void handleErrorResponse(RoutingContext context, Throwable exception, Function<ResponseBean, String> responseFormatter) {
		ResponseType responseType = ResponseType.JSON;
		if ("application/xml".equals(context.getAcceptableContentType())) {
			responseType = ResponseType.XML;
			context.response().putHeader("Access-Control-Allow-Headers", "Content-Type,application/xml");
		} else {
			context.response().putHeader("Access-Control-Allow-Headers", "Content-Type,application/json");
		}
		// Adding CORS headers.
		context.response().putHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,DELETE");
		context.response().putHeader("Access-Control-Allow-Headers",
				"Authorization,Origin,X-Requested-With,Content-Type,Accept,merchantAccessKey,requestSignature");
		context.response().putHeader("Access-Control-Max-Age", "3600");
		context.response().putHeader("Access-Control-Allow-Origin", "*");
		context.response().setChunked(true);
		String response = getErrorResponse(responseType, exception, responseFormatter);
		// Sending response
		context.response().end(response);
	}

	public static String getErrorResponse(ResponseType responseType, Throwable exception, Function<ResponseBean, String> responseFormatter) {
		// Default error response
		String response = null;

		int responseCode = Result.FAIL.getResultCode();
		String responseMessage = Result.FAIL.getResultMessage();
		Object data = null;

		if (exception == null)
			exception = new ApplicationException();

		// Custom error response
		if (exception instanceof AbstractException) {
			AbstractException absException = (AbstractException) exception;
			responseCode = absException.getErrorCode();
			responseMessage = absException.getErrorMessage();
			data = absException.getData();
		} else {
			LOGGER.error("Some error has occured", exception);
		}

		// response formatting
		if (responseFormatter != null) {
			response = responseFormatter.apply(new ResponseBean(responseCode, responseMessage, data));
		} else {
			response = errorResponse(responseCode, responseMessage, data, responseType);
		}

		// Logging errors
		LOGGER.error("Error Response: {} ", response);

		return response;
	}

	private static String errorResponse(int errorcode, String errorMessage, Object data, ResponseType respType) {
		if (respType == ResponseType.XML)
			return errorResponseXML(errorcode, errorMessage, data);

		return errorResponseJSON(errorcode, errorMessage, data);
	}

	private static String errorResponseJSON(int errorcode, String errorMessage, Object data) {
		return BoonJsonUtil.serialize(new ResponseBean(errorcode, errorMessage, data));
	}

	private static String errorResponseXML(int errorcode, String errorMessage, Object data) {
		ResponseBean responseBean = new ResponseBean(errorcode, errorMessage, data);

		try {
			JAXBContext context = JAXBContext.newInstance(ResponseBean.class);
			Marshaller m = context.createMarshaller();
			// for pretty-print XML in JAXB
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			m.marshal(responseBean, sw);
			return sw.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
			return "Internal Error";
		}
	}
}