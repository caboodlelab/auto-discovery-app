package com.caboodle.enums;

import com.caboodle.util.CommonUtil;


public enum Result {

	SUCCESS(0, "SUCCESS", Status.SUCCESS), 
	FAIL(1, "FAIL", Status.FAIL), 
	INITIATED(2, "Initiated", Status.INITIATED), 
	INTERNAL_ERROR(3, "Internal error", Status.INITIATED), 
	CANCELED(4,"cancelled", Status.CANCELED);
	


	private int resultCode;
	private String resultMessage;
	private Status status;

	private Result(int resultCode, String resultMessage, Status status) {
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
		this.status = status;
	}

	public int getResultCode() {
		return resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public Status getTransactionStatus() {
		return status;
	}

	public static Result getStatus(String status) {
		if (CommonUtil.isNotEmpty(status)) {
			try {
				return Result.valueOf(status.toUpperCase());
			} catch (Exception e) {
				return FAIL;
			}
		}
		return FAIL;
	}
}
