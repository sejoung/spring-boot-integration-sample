package com.github.sejoung.integration.endpoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.StreamUtils;

@Slf4j
@MessageEndpoint
public class FTPUploadFileEndpoint {

	@ServiceActivator
	public Object handle(File input, MessageHeaders messageHeaders) throws IOException {

		try (FileInputStream in = new FileInputStream(input)) {

			String data = new String(StreamUtils.copyToByteArray(in));


			log.debug("upload data = {}", data);

		}
		return null;
	}
}
