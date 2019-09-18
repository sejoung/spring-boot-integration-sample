package com.github.sejoung.integration.endpoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StreamUtils;

@Slf4j
@MessageEndpoint
public class FTPFileEndpoint {

	@ServiceActivator
	public Message<File> handle(File input, MessageHeaders messageHeaders) throws IOException {

		try (FileInputStream in = new FileInputStream(input)) {
			String data = new String(StreamUtils.copyToByteArray(in));
			log.debug("AbsolutePath = {} CanonicalPath = {}", input.getAbsolutePath(),
					input.getCanonicalPath());
			log.debug("filedata = {}", data);
		}

		messageHeaders.forEach((k, v) ->
				log.info(k + ':' + v)
		);

		if ("hello.txt".equals(messageHeaders.get("file_name"))) {
			var message = MessageBuilder.withPayload(new File(input.getParent(), "test.txt")).build();
			return message;
		}

		return null;
	}
}
