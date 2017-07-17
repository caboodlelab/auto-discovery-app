package com.caboodle.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.caboodle.runner.ApplicationMainRunner;
import io.vertx.core.file.FileSystem;


/**
 * @author harishchauhan
 *
 */
public class AppConfig {
	
	private final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

	private Properties properties;

	private final String appBaseConfigDir;

	public AppConfig(String configDir) {
		this.appBaseConfigDir = configDir;
		loadPropertiesFile();
	}

	public Properties getProperties() {
		return properties;
	}

	private void loadPropertiesFile() {
		properties = new Properties();
		FileSystem fs = ApplicationMainRunner.getVertxInstance().fileSystem();
		String filePath = appBaseConfigDir + File.separatorChar + "app.properties";
		InputStream in = new ByteArrayInputStream(fs.readFileBlocking(filePath).getBytes());
		try {
			properties.load(in);
			LOGGER.info("Loaded {} application properties", properties.size());
		} catch (IOException e) {
			LOGGER.error("Loading of application properties failed", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				LOGGER.error("Closing of input stream  for application properties failed", e);
			}
		}
	}

	/**
	 * Returns properties with key/value pair for all keys that starts with
	 * <b>prefix</b> and separated by DOT (.). Returned properties contains keys
	 * with removed prefix.
	 * 
	 * @param prefix
	 * @return
	 */
	public Properties getPropertiesWithPrefix(String prefix) {
		Properties p = new Properties();
		Enumeration<Object> keys = properties.keys();
		while (keys.hasMoreElements()) {
			String k = keys.nextElement().toString();
			String prefix2 = prefix + ".";
			if (k.startsWith(prefix2)) {
				String substring = k.substring(prefix2.length());
				if (substring.length() > 0)
					p.put(substring, properties.get(k));
			}
		}
		return p;
	}

	public String getValue(String key) {
		return (String) properties.get(key);
	}

}
