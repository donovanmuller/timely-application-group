package io.switchbit;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;

@EnableBinding(Processor.class)
@EnableConfigurationProperties(TimezoneConfigurationProperties.class)
public class TimezoneProcessor {

	private static final Logger log = LoggerFactory.getLogger(TimezoneProcessor.class);

	private TimezoneConfigurationProperties timezoneConfigurationProperties;

	public TimezoneProcessor(TimezoneConfigurationProperties timezoneConfigurationProperties) {
		this.timezoneConfigurationProperties = timezoneConfigurationProperties;
	}

	/**
	 * Convert the <code>time</code> message received on the Processor.INPUT channel to the
	 * configured timezone. <b>The <code>time</code> is expected to be in UTC.</b>
	 * 
	 * @param time a String expected in the configured time format.
	 * {@link TimezoneConfigurationProperties#getInputTimeFormat()}
	 * @return a String representing the time in the configured timezone
	 */
	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	String convertTimezone(String time) {
		log.info("Converting time '{}' to timezone: '{}'", time, timezoneConfigurationProperties.getTimezone());

		DateTimeFormatter timeFormatter = DateTimeFormatter
				.ofPattern(timezoneConfigurationProperties.getInputTimeFormat()).withZone(ZoneId.of("UTC"));
		ZonedDateTime convertedTime = ZonedDateTime.parse(time, timeFormatter)
				.withZoneSameInstant(ZoneId.of(timezoneConfigurationProperties.getTimezone()));
		return convertedTime.format(DateTimeFormatter.ofPattern(timezoneConfigurationProperties.getOutputTimeFormat()));
	}
}
