package com.caboodle.runner;

import java.util.Set;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caboodle.annotation.UrlPath;
import com.caboodle.util.CommonUtil;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * @author harishchauhan
 *
 */
public class Verticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(Verticle.class);

	@Override
	public void start() throws Exception {
		try {
			long startTime = System.currentTimeMillis();
			Router mainRouter = Router.router(vertx);
			ServiceLocator locator = registerBeans();
			loadWebApis(locator, mainRouter);
			WebServer server = locator.getService(WebServer.class);
			server.start(mainRouter);
			LOGGER.info("Middleware application started in {} ms", System.currentTimeMillis() - startTime);
		} catch (Exception e) {
			LOGGER.error("Cannot start application ", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void loadWebApis(ServiceLocator locator, Router mainRouter) {
		// Adding a BodyHandler for routes
		mainRouter.route().handler(BodyHandler.create());
		Reflections reflections = new Reflections("com.caboodle.api");
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(UrlPath.class);
		for (Class<?> rit : annotated) {
			try {
				Object service = locator.getService(rit);
				if (service != null) {
					Router subRouter = Router.router(vertx);
					UrlPath urlPath = service.getClass().getAnnotation(UrlPath.class);
					
					// CORS handling in each route
					subRouter.route(HttpMethod.OPTIONS, urlPath.name()).handler(context -> {
						HttpServerResponse response = context.response();
						response.putHeader("Content-Length", "0");
						response.putHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,DELETE");
						response.putHeader("Access-Control-Max-Age", "3600");
						response.putHeader("Access-Control-Allow-Headers", "Authorization,Origin,X-Requested-With,Content-Type,Accept,merchantAccessKey,requestSignature");
						response.putHeader("Access-Control-Allow-Origin", "*");
						response.setStatusCode(200).end();

						//context.response().end();
					});
					
					Route route = null;
					if (!CommonUtil.isEmpty(urlPath.method()))
						route = subRouter.route(HttpMethod.valueOf(urlPath.method()), urlPath.name());
					else
						route = subRouter.route(urlPath.name());
					if (!service.getClass().getAnnotation(UrlPath.class).consumes().isEmpty())
						route.consumes(service.getClass().getAnnotation(UrlPath.class).consumes());
					if (!service.getClass().getAnnotation(UrlPath.class).produces().isEmpty())
						route.produces(service.getClass().getAnnotation(UrlPath.class).produces());
					route.handler((Handler<RoutingContext>) service);
					mainRouter.mountSubRouter("/", subRouter);
				}
			} catch (Exception ex) {
				LOGGER.error("Error starting routes", ex);
			}
		}
	}


	private ServiceLocator registerBeans() {
		ServiceLocator locator = ServiceLocatorUtilities.createAndPopulateServiceLocator();
		ServiceLocatorUtilities.bind(locator, new BootstrapBinder(vertx, locator, "com.caboodle"));
		return locator;
	}
}

