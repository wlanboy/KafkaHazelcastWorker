package com.wlanboy.hazelcastwork.exceptions;

public class CancelledWorkHazelcastInstanceException extends RuntimeException {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8252405898125617157L;

	public CancelledWorkHazelcastInstanceException(String message, String workid) {
		super(String.format("Work with workid %s failed with reason %s.", workid, message));
	}
}