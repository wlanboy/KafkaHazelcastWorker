package com.wlanboy.hazelcastwork;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@EnableBinding(Source.class)
@Controller
public class SourceConfiguration {

	private static final Logger logger = Logger.getLogger(SourceConfiguration.class.getCanonicalName());
	
	@Autowired
	private Source channel;
	
	@RequestMapping(value = "/job", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void handleRequest(@RequestBody String body, @RequestHeader Map<String, Object> headers) {
		logger.info(body);
		MessageHeaders header = new MessageHeaders(headers);
		channel.output().send(MessageBuilder.createMessage(body,header));
	}

}
