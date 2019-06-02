package com.wlanboy.hazelcastwork.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wlanboy.hazelcastwork.HazelcastWorkDto;
import com.wlanboy.hazelcastwork.service.WorkHazelcastService;

/**
 * The HazelcastWork resource controller.
 */
@RestController
public class HazelcastWorkController {

	private final WorkHazelcastService service;

	@Autowired
	public HazelcastWorkController(WorkHazelcastService service) {
		this.service = service;
	}

	@RequestMapping(value = "/work", method = RequestMethod.GET)
	public HttpEntity<List<HazelcastWorkDto>> getJobList(){
		List<HazelcastWorkDto> joblist = service.getHazelcastWorkDtoList();

		return new ResponseEntity<>(joblist, HttpStatus.OK);
	}

	@RequestMapping(value = "/work", method = RequestMethod.POST)
	public HttpEntity<HazelcastWorkDto> addJob(@RequestBody HazelcastWorkDto job) {
		String workid = service.submit(job);
		job = service.getHazelcastWorkDto(workid);

		return new ResponseEntity<HazelcastWorkDto>(job, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/work/{workid}", method = RequestMethod.GET)
	public HttpEntity<HazelcastWorkDto> getJob(@PathVariable("workid") String workid) {
		HazelcastWorkDto status = service.getHazelcastWorkDto(workid);

		if (null == status) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<HazelcastWorkDto>(status, HttpStatus.OK);
	}

	@RequestMapping(value = "/work/{workid}", method = RequestMethod.PUT)
	public HttpEntity<HazelcastWorkDto> rerunJob(@PathVariable("workid") String workid) {
		HazelcastWorkDto status = service.getHazelcastWorkDto(workid);

		if (null == status) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		service.rerunHazelcastWorkDto(workid);

		return new ResponseEntity<>(HttpStatus.OK);
	}	
	
	@RequestMapping(value = "/work/{workid}", method = RequestMethod.DELETE)
	public HttpEntity<HazelcastWorkDto> deleteJob(@PathVariable("workid") String workid) {
		HazelcastWorkDto RandomJobStatus = service.getHazelcastWorkDto(workid);

		if (null == RandomJobStatus) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		service.cancelHazelcastWork(workid);

		return new ResponseEntity<>(HttpStatus.OK);
	}

}