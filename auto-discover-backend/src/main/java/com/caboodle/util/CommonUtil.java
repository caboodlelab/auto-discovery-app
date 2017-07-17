package com.caboodle.util;

import java.io.Closeable;
import java.io.StringReader;
import java.util.Collection;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author harishchauhan
 *
 */
public class CommonUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);

	public static boolean isNotNull(Object... args) {
		for (Object s : args) {
			if (s == null || (s != null && s.toString().equalsIgnoreCase(Constants.EMPTY_STRING))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(String str) {
		return (str == null || str.trim().length() <= 0);
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isNull(Object object) {
		return (object == null) ? Boolean.TRUE : Boolean.FALSE;
	}

	public static boolean isCollectionEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	/**
	 * converts XML to Java object.
	 * 
	 * @param String
	 * @return
	 */
	public static Object XMLtoJaxObject(String xml) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Object.class);
			StringReader stringReader = new StringReader(xml);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Object object = (Object) jaxbUnmarshaller.unmarshal(stringReader);
			return object;
		} catch (JAXBException e) {
			LOGGER.error("Error while converting java object to XML", e);
		}
		return null;
	}

	public static boolean isMapEmpty(Map<?, ?> map) {
		return (map == null || map.isEmpty());
	}


	public static void closeResources(Closeable c) {
		try {
			c.close();
		} catch (Exception e) {
			LOGGER.error("Error occured while closing resources: {}", e.getMessage());
		}
	}

}
