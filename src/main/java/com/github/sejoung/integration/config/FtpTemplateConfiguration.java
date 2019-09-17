package com.github.sejoung.integration.config;

import java.io.File;
import java.io.FileOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;


@Slf4j
//@Configuration
public class FtpTemplateConfiguration {

	//@Bean
	InitializingBean initializingBean(FtpRemoteFileTemplate template) {
		return () -> template
				.execute(session -> {
					var file = new File(new File(System.getProperty("user.home"), "Desktop"),
							"hello-local.txt");
					try (var fout = new FileOutputStream(file)) {
						session.read("hello.txt", fout);
					}
					log.info("read " + file.getAbsolutePath());
					return null;
				});
	}



	//@Bean
	FtpRemoteFileTemplate ftpRemoteFileTemplate(DefaultFtpSessionFactory dsf) {
		return new FtpRemoteFileTemplate(dsf);
	}

}
