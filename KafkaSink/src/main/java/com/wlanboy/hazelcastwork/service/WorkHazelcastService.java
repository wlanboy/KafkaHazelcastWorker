package com.wlanboy.hazelcastwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;

@Service
public class WorkHazelcastService extends WorkHazelcastInstanceBaseService {

	@Autowired
	public WorkHazelcastService(HazelcastInstance hcInstance) {
		this.instance = hcInstance;
		getHazelcastWokDtoMap().addEntryListener(new HazelcastWorkDtoMapListener(), true);
	}
}