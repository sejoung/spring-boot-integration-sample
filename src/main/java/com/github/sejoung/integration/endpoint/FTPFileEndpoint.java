package com.github.sejoung.integration.endpoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.StreamUtils;

@Slf4j
@RequiredArgsConstructor
@MessageEndpoint
public class FTPFileEndpoint {


	private final DefaultFtpSessionFactory ftpSf;

	@ServiceActivator
	public File handle(File input, MessageHeaders messageHeaders) throws IOException {

		try (FileInputStream in = new FileInputStream(input)) {
			String data = new String(StreamUtils.copyToByteArray(in));
			log.debug("AbsolutePath = {} CanonicalPath = {}", input.getAbsolutePath(),
					input.getCanonicalPath());
			log.debug("filedata = {}", data);
		}

		messageHeaders.forEach((k, v) ->
				log.info(k + ':' + v)
		);
		if("hello.txt".equals(messageHeaders.get("file_name"))){
			return new File(input.getParent(), "test.txt");
		}

		return null;
	}
}
