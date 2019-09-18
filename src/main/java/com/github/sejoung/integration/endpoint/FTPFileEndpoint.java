package com.github.sejoung.integration.endpoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.StreamUtils;

@Slf4j
@MessageEndpoint
public class FTPFileEndpoint {

	@ServiceActivator
	public Object handle(File input, MessageHeaders messageHeaders) throws IOException {

		try (FileInputStream in = new FileInputStream(input)) {
			String data = new String(StreamUtils.copyToByteArray(in));
			log.debug("filedata = {}", data);
		}
		messageHeaders.forEach((k, v) ->
				log.info(k + ':' + v)
		);
		return null;
	}
}
