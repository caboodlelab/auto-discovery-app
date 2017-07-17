package com.caboodle.runner;

import org.jvnet.hk2.annotations.Contract;

import io.vertx.ext.web.Router;

/**
 * @author harishchauhan
 *
 */
@Contract
public interface WebServer {

	public void start(Router mainRouter);
	
	public void stop();
}