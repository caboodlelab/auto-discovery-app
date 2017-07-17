package com.caboodle.enums;

/**
 * @author harishchauhan
 *
 */
public enum Status {

	SUCCESS(0), FAIL(1), INITIATED(2), CANCELED(3),INTERNAL_ERROR(4);

	private Status(int code) {
		this.code = code;
	}

	private int code;

	/**
	 * @return code
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * @param status
	 * @return returns the transaction status.
	 */
	public static Status getStatus(String status) {
		try {
			return Status.valueOf(status.toUpperCase());
		} catch (Exception e) {
			return FAIL;
		}
	}
}
