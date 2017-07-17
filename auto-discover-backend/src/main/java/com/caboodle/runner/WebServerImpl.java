package com.caboodle.runner;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caboodle.util.ConfigManager;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

/**
 * @author harishchauhan
 *
 */
@Service
public class WebServerImpl implements WebServer {

	private final static Logger LOGGER = LoggerFactory.getLogger(WebServerImpl.class);

	private HttpServer httpServer;

	@Inject
	private Vertx vertx;

	@Override
	public void start(Router mainRouter) {
		int port = Integer.parseInt(ConfigManager.INSTANCE.getAppConfig().getProperties().getProperty("app.port", "8090"));
		int frameSize = Integer.parseInt(ConfigManager.INSTANCE.getAppConfig().getProperties().getProperty("app.server.framesize", "1000000"));
		HttpServerOptions options = new HttpServerOptions().setMaxWebsocketFrameSize(frameSize);
		HttpServer httpServer = vertx.createHttpServer(options);
		httpServer.requestHandler(mainRouter::accept).listen(port);
		LOGGER.info("Http server listening on port {}", port);
	}


	@Override
	public void stop() {
		if (httpServer != null)
			httpServer.close();
	}
}

