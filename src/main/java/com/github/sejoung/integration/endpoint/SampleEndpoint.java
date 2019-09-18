package com.github.sejoung.integration.endpoint;

import com.github.sejoung.integration.service.HelloWorldService;
import java.io.File;
import java.io.FileInputStream;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.StreamUtils;

@MessageEndpoint
public class SampleEndpoint implements GenericHandler {

	private final HelloWorldService helloWorldService;

	public SampleEndpoint(HelloWorldService helloWorldService) {
		this.helloWorldService = helloWorldService;
	}

	@ServiceActivator
	public String hello(File input) throws Exception {
		FileInputStream in = new FileInputStream(input);
		String name = new String(StreamUtils.copyToByteArray(in));
		in.close();
		return this.helloWorldService.getHelloMessage(name);
	}

	@Override
	public Object handle(Object o, MessageHeaders messageHeaders) {
		return null;
	}
}
