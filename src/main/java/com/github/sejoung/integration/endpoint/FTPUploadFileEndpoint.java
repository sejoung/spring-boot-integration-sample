package com.github.sejoung.integration.endpoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.StreamUtils;

@Slf4j
@MessageEndpoint
@RequiredArgsConstructor
public class FTPUploadFileEndpoint {

	private final FtpRemoteFileTemplate template;


	@ServiceActivator
	public Object handle(File input, MessageHeaders messageHeaders) throws IOException {
		var filename = "test_re.txt";
		try (FileInputStream in = new FileInputStream(input)) {
			String data = new String(StreamUtils.copyToByteArray(in));
			log.debug("upload data = {}", data);
		}

		try (FileInputStream in = new FileInputStream(input)) {
			log.debug("업로드시작");
			template.execute((c) -> {

				if (c.exists(filename)) {
					log.debug("파일삭제후 재업로드");
					c.remove(filename);
				}

				c.write(in, filename);
				c.close();
				return null;
			});
		}

		return null;
	}
}
