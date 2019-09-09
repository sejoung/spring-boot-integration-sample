package com.github.sejoung.integration.gateway;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "outputChannel")
public interface SampleMessageGateway {

	void echo(String message);

}
