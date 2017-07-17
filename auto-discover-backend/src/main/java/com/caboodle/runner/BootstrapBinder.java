package com.caboodle.runner;

import java.util.Set;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.jvnet.hk2.annotations.Service;
import org.reflections.Reflections;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

/**
 * @author harishchauhan
 *
 */
public class BootstrapBinder extends AbstractBinder {

	private Vertx vertx;
	private String packageToScan;
	private ServiceLocator locator;
	
	public BootstrapBinder(Vertx vertx, ServiceLocator locator, String packageToScan) {
		this.vertx = vertx;
		this.locator = locator;
		this.packageToScan = packageToScan;
	}
	
	@Override
	protected void configure() {
		bind(vertx).to(Vertx.class);
		bind(vertx.eventBus()).to(EventBus.class);
		loadServices(packageToScan);
	}
	
	private void loadServices(String packageToScan) {
		// Add services to locator
		Reflections reflections = new Reflections(packageToScan);
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Service.class);
		for (Class<?> configuredBy : annotated) {
			ServiceLocatorUtilities.addClasses(locator, configuredBy);
		}
	}
}
