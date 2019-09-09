package com.github.sejoung.integration.service;

import com.github.sejoung.integration.constants.ServiceProperties;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {

	private final ServiceProperties configuration;

	public HelloWorldService(ServiceProperties configuration) {
		this.configuration = configuration;
	}

	public String getHelloMessage(String name) {
		return this.configuration.getGreeting() + " " + name;
	}

}
