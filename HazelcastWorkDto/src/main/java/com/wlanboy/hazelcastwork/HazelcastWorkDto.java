package com.wlanboy.hazelcastwork;

import java.io.Serializable;

public class HazelcastWorkDto implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4618740127338310688L;

	private String id;
	private String workerid;
	private String work;
	private String result;
	private WorkState state;
	private String message;
	
	public HazelcastWorkDto() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWorkerid() {
		return workerid;
	}

	public void setWorkerid(String workerid) {
		this.workerid = workerid;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public WorkState getState() {
		return state;
	}

	public void setState(WorkState state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}