package com.github.sejoung.integration.flow;

import static org.apache.commons.net.ftp.FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE;

import com.github.sejoung.integration.endpoint.FTPFileEndpoint;
import com.github.sejoung.integration.endpoint.FTPUploadFileEndpoint;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.remote.gateway.AbstractRemoteFileOutboundGateway.Option;
import org.springframework.integration.ftp.dsl.Ftp;
import org.springframework.integration.ftp.gateway.FtpOutboundGateway;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.messaging.MessageChannel;

@Slf4j
@Configuration
public class FlowConfiguration {

	@Bean
	IntegrationFlow inbound(DefaultFtpSessionFactory ftpSf, FTPFileEndpoint ftpFileEndpoint,
			MessageChannel ftpGet) {
		var localDirectory = new File(new File(System.getProperty("user.home"), "Desktop"),
				LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

		var spec = Ftp
				.inboundAdapter(ftpSf)
				.autoCreateLocalDirectory(true)
				.patternFilter("hello.txt")
				.localDirectory(localDirectory);

		return IntegrationFlows
				.from(spec, pc -> pc.poller(pm -> pm.fixedRate(1, TimeUnit.SECONDS)))
				.handle(ftpFileEndpoint).channel(ftpGet).get();
	}

	@Bean
	IntegrationFlow outbound(DefaultFtpSessionFactory ftpSf,
			FTPUploadFileEndpoint ftpUploadFileEndpoint) {

		return IntegrationFlows
				.from(ftpGet())
				.handle(ftpUploadFileEndpoint).get();
	}

	@Bean
	MessageChannel ftpGet() {
		return MessageChannels.direct().get();
	}

	@Bean
	FtpRemoteFileTemplate ftpRemoteFileTemplate(DefaultFtpSessionFactory dsf) {
		FtpRemoteFileTemplate template = new FtpRemoteFileTemplate(dsf);
		return template;
	}

	@Bean
	DefaultFtpSessionFactory defaultFtpSessionFactory(
			@Value("${ftp.username}") String username,
			@Value("${ftp.password}") String pw,
			@Value("${ftp.host}") String host,
			@Value("${ftp.port}") int port) {
		DefaultFtpSessionFactory defaultFtpSessionFactory = new DefaultFtpSessionFactory();
		defaultFtpSessionFactory.setPassword(pw);
		defaultFtpSessionFactory.setUsername(username);
		defaultFtpSessionFactory.setHost(host);
		defaultFtpSessionFactory.setPort(port);
		defaultFtpSessionFactory.setControlEncoding("utf-8");
		defaultFtpSessionFactory.setClientMode(PASSIVE_LOCAL_DATA_CONNECTION_MODE);

		return defaultFtpSessionFactory;
	}

}
