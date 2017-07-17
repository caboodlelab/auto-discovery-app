package com.caboodle.runner;

import io.vertx.core.Vertx;

/**
 * @author harishchauhan
 *
 */
public class ApplicationMainRunner {
	private static Vertx vertx = Vertx.vertx();

	public static Vertx getVertxInstance() {
		return vertx;
	}

	public static void main(String... args) {
		vertx.deployVerticle("com.caboodle.runner.Verticle");
	}

}
