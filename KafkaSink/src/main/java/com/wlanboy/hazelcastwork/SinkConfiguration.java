package com.wlanboy.hazelcastwork;

import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import com.wlanboy.hazelcastwork.service.WorkHazelcastService;

@EnableBinding(Sink.class)
public class SinkConfiguration {

	private static final Logger logger = Logger.getLogger(SinkConfiguration.class.getCanonicalName());
	
	@Autowired
	WorkHazelcastService service;
	
    @ServiceActivator(inputChannel=Sink.INPUT)
    public void loggerSink(Message<HazelcastWorkDto> workdto) {
    	HazelcastWorkDto playload = workdto.getPayload();
        logger.info(playload.toString());
        String headers = workdto.getHeaders().entrySet()
                .stream()
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining(", "));
        logger.info(headers);
        service.submit(playload);
    }
}
