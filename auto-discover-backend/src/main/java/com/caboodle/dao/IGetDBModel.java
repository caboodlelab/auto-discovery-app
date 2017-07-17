package com.caboodle.dao;

import java.util.List;
import java.util.Map;

import org.jvnet.hk2.annotations.Contract;

import com.caboodle.entity.DBEntity;

import io.vertx.core.Future;

/**
 * @author harishchauhan
 *
 */
@Contract
public interface IGetDBModel {
	public Future<List<DBEntity>> fetchByFind(Map<String, Object> param);
}
