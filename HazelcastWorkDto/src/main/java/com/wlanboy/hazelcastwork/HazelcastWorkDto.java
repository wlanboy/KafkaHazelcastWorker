package com.wlanboy.hazelcastwork;

import java.io.Serializable;

import lombok.Data;

@Data
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
}