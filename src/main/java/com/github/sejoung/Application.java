package com.github.sejoung;

import com.github.sejoung.integration.SampleEndpoint;
import com.github.sejoung.integration.ServiceProperties;
import java.io.File;
import java.util.function.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.SourcePollingChannelAdapterSpec;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;

@SpringBootApplication
@EnableConfigurationProperties(ServiceProperties.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public FileReadingMessageSource fileReader() {
		FileReadingMessageSource reader = new FileReadingMessageSource();
		reader.setDirectory(new File("target/input"));
		return reader;
	}

	@Bean
	public DirectChannel inputChannel() {
		return new DirectChannel();
	}

	@Bean
	public DirectChannel outputChannel() {
		return new DirectChannel();
	}

	@Bean
	public FileWritingMessageHandler fileWriter() {
		FileWritingMessageHandler writer = new FileWritingMessageHandler(new File("target/output"));
		writer.setExpectReply(false);
		return writer;
	}

	@Bean
	public IntegrationFlow integrationFlow(SampleEndpoint endpoint) {
		return IntegrationFlows.from(fileReader(), new FixedRatePoller()).channel(inputChannel())
				.handle(endpoint)
				.channel(outputChannel()).handle(fileWriter()).get();
	}

	private static class FixedRatePoller implements Consumer<SourcePollingChannelAdapterSpec> {

		@Override
		public void accept(SourcePollingChannelAdapterSpec spec) {
			spec.poller(Pollers.fixedRate(500));
		}

	}

}