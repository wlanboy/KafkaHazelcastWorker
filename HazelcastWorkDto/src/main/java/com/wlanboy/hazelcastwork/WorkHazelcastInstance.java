package com.wlanboy.hazelcastwork;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.Callable;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.wlanboy.hazelcastwork.config.HazelcastConfigurationStatics;
import com.wlanboy.hazelcastwork.exceptions.CancelledWorkHazelcastInstanceException;

import lombok.Getter;
import lombok.Setter;

public class WorkHazelcastInstance implements Callable<HazelcastWorkDto>, HazelcastInstanceAware, Serializable {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5344451361539778804L;

	@Getter
    private HazelcastInstance instance;
  
	@Setter
	@Getter
    private String uuid;
	
	@Setter
	@Getter
    private String work;	
	
	private int wait = 10000;
  
    public WorkHazelcastInstance(String uuid, String work) {
      this.uuid = uuid;
      this.work = work;
    }
  
    @Override
    public void setHazelcastInstance(HazelcastInstance instance) {
      this.instance = instance;
    }
  
    @Override
    public HazelcastWorkDto call() throws InterruptedException {
  
      if (WorkState.CANCELLED.equals(getJobStatus())) {
        throw new CancelledWorkHazelcastInstanceException("HazelcastWorkDto was cancelled.", uuid);
      }
      HazelcastWorkDto result = processWork(WorkState.RUNNING, new Random().nextLong());
      
      return result;
    }
  
    private HazelcastWorkDto processWork(WorkState state, long nextLong) throws InterruptedException {
        HazelcastWorkDto result = getWorkDTO();
        result.setState(state);
        result.setResult(String.valueOf(nextLong));
        result.setWorkerid(instance.getName());
        
        instance.getMap(HazelcastConfigurationStatics.WORK_MAP).put(uuid, result);
        
        Thread.sleep(wait);
		return result;
	}

	public HazelcastWorkDto getWorkDTO() {
        return ((HazelcastWorkDto) instance.getMap(HazelcastConfigurationStatics.WORK_MAP).get(uuid));
      }
    
    public WorkState getJobStatus() {
      return getWorkDTO().getState();
    }
  
  }