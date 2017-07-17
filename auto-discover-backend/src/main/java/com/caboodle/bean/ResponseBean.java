package com.caboodle.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.boon.json.annotations.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author harishchauhan
 *
 */
@XmlRootElement(name = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private int respCode;
	private String respMsg;
	private Object data;

	public ResponseBean() {}

	public ResponseBean(int respCode, String respMsg) {
		this.respCode = respCode;
		this.respMsg = respMsg;
	}

	public ResponseBean(int respCode, String respMsg, Object data) {
		super();
		this.respCode = respCode;
		this.respMsg = respMsg;
		this.data = data;
	}

	public int getRespCode() {
		return respCode;
	}

	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	@JsonIgnore
	@XmlTransient
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
