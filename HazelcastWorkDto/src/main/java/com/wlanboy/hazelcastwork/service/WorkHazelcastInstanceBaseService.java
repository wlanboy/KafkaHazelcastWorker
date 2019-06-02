package com.wlanboy.hazelcastwork.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.wlanboy.hazelcastwork.HazelcastWorkDto;
import com.wlanboy.hazelcastwork.WorkHazelcastInstance;
import com.wlanboy.hazelcastwork.WorkState;
import com.wlanboy.hazelcastwork.config.HazelcastConfigurationStatics;
import com.wlanboy.hazelcastwork.exceptions.CancelledWorkHazelcastInstanceException;

public class WorkHazelcastInstanceBaseService {
	protected HazelcastInstance instance = null;

	public String submit(HazelcastWorkDto workdto) {
		final String taskUUID = UUID.randomUUID().toString();
		IExecutorService executorService = instance.getExecutorService(HazelcastConfigurationStatics.WORK_SERVICE);

		workdto.setState(WorkState.ADDED);
		workdto.setId(taskUUID);

		getHazelcastWokDtoMap().put(taskUUID, workdto);

		WorkHazelcastInstance job = new WorkHazelcastInstance(taskUUID, workdto.getWork());
		submitJob(taskUUID, executorService, job);

		return taskUUID;
	}

	public HazelcastWorkDto getHazelcastWorkDto(String taskUUID) {
		HazelcastWorkDto status = getHazelcastWokDtoMap().get(taskUUID);

		if (null == status)
			return null;

		return status;
	}

	public HazelcastWorkDto getHazelcastWorkDtoResult(String taskUUID) {
		HazelcastWorkDto status = getHazelcastWorkDtoResultMap().get(taskUUID);

		if (null == status)
			return null;

		return status;
	}

	public void rerunHazelcastWorkDto(String taskUUID) {
		HazelcastWorkDto workdto = getHazelcastWokDtoMap().get(taskUUID);

		if (null == workdto)
			return;

		if (WorkState.ERROR.equals(workdto.getState())) {
			IExecutorService executorService = instance.getExecutorService(HazelcastConfigurationStatics.WORK_SERVICE);

			getHazelcastWokDtoMap().remove(taskUUID);
			getHazelcastWorkDtoResultMap().remove(taskUUID);

			workdto.setState(WorkState.ADDED);

			getHazelcastWokDtoMap().put(taskUUID, workdto);

			WorkHazelcastInstance job = new WorkHazelcastInstance(taskUUID, workdto.getWork());
			submitJob(taskUUID, executorService, job);
		}
	}

	public void cancelHazelcastWork(String taskUUID) {
		HazelcastWorkDto workdto = getHazelcastWokDtoMap().get(taskUUID);

		if (null == workdto)
			return;

		if (WorkState.FINISHED.equals(workdto.getState())
				|| WorkState.ERROR.equals(workdto.getState())) {
			getHazelcastWokDtoMap().remove(taskUUID);
			getHazelcastWorkDtoResultMap().remove(taskUUID);
		} else {
			workdto.setState(WorkState.CANCELLED);
			getHazelcastWokDtoMap().put(taskUUID, workdto);
		}

	}

	protected IMap<String, HazelcastWorkDto> getHazelcastWokDtoMap() {
		return instance.getMap(HazelcastConfigurationStatics.WORK_MAP);
	}

	protected IMap<String, HazelcastWorkDto> getHazelcastWorkDtoResultMap() {
		return instance.getMap(HazelcastConfigurationStatics.WORK_RESULT_MAP);
	}

	public ArrayList<HazelcastWorkDto> getHazelcastWorkDtoList() {
		return new ArrayList<HazelcastWorkDto>(getHazelcastWokDtoMap().values());
	}

	public Page<HazelcastWorkDto> getHazelcastWorkDtoListPaged(PageRequest pageRequest) {
		ArrayList<HazelcastWorkDto> joblist = new ArrayList<HazelcastWorkDto>(getHazelcastWokDtoMap().values());
		int start = (int) pageRequest.getOffset();
		int end = (start + pageRequest.getPageSize()) > joblist.size() ? joblist.size() : (start + pageRequest.getPageSize());

		Page<HazelcastWorkDto> pages = new PageImpl<HazelcastWorkDto>(joblist.subList(start, end), pageRequest, joblist.size());
		return pages;
	}

	private void submitJob(final String jobid, IExecutorService executorService, WorkHazelcastInstance job) {
		executorService.submit(job, new ExecutionCallback<HazelcastWorkDto>() {

			@Override
			public void onResponse(HazelcastWorkDto response) {
				getHazelcastWorkDtoResultMap().put(jobid, response);

				HazelcastWorkDto status = getHazelcastWorkDto(jobid);
				status.setState(WorkState.FINISHED);
				status.setResult(response.getResult());
				status.setWorkerid(response.getWorkerid());
				getHazelcastWokDtoMap().put(jobid, status);
			}

			@Override
			public void onFailure(Throwable t) {
				HazelcastWorkDto randomJobStatus = getHazelcastWorkDto(jobid);

				if (t instanceof CancelledWorkHazelcastInstanceException) {
					getHazelcastWokDtoMap().remove(jobid);
					getHazelcastWorkDtoResultMap().remove(jobid);
					return;
				}

				if (randomJobStatus != null) {
					randomJobStatus.setState(WorkState.ERROR);
					// RandomJobStatus.setMessage(t.getMessage());
					getHazelcastWokDtoMap().put(jobid, randomJobStatus);
				}
			}
		});
	}
}
