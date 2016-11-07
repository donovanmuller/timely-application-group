package io.switchbit;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties
public class TimelyEventConfigurationProperties {

	@Getter
	@Setter
	private Long sseTimeout = 10000L;

}
