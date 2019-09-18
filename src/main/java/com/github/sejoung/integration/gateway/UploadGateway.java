package com.github.sejoung.integration.gateway;

import java.io.File;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "incoming")
public interface UploadGateway {

	void upload(File file);

}
