package io.switchbit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TimezoneConfigurationProperties.class)
public class TimezoneConverterConfiguration {

	private TimezoneConfigurationProperties timezoneConfigurationProperties;

	public TimezoneConverterConfiguration(TimezoneConfigurationProperties timezoneConfigurationProperties) {
		this.timezoneConfigurationProperties = timezoneConfigurationProperties;
	}

	@Bean
	@ConditionalOnMissingBean
	public TimezoneConverter timezoneConverter() {
		return new TimezoneConverter(timezoneConfigurationProperties);
	}
}
