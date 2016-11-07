package io.switchbit;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimezoneConverter {

	private static final Logger log = LoggerFactory.getLogger(TimezoneConverter.class);

	private TimezoneConfigurationProperties timezoneConfigurationProperties;

	public TimezoneConverter(final TimezoneConfigurationProperties timezoneConfigurationProperties) {
		this.timezoneConfigurationProperties = timezoneConfigurationProperties;
	}

	/**
	 * Convert the <code>time</code> value to the configured timezone. <b>The <code>time</code> is
	 * expected to be in UTC.</b>
	 *
	 * @param time a String expected in the configured time format.
	 * {@link TimezoneConfigurationProperties#getInputTimeFormat()}
	 * @return a String representing the time in the configured timezone
	 */
	public String convert(String time) {
		log.info("Converting time '{}' to timezone: '{}'", time, timezoneConfigurationProperties.getTimezone());

		DateTimeFormatter timeFormatter = DateTimeFormatter
				.ofPattern(timezoneConfigurationProperties.getInputTimeFormat()).withZone(ZoneId.of("UTC"));
		ZonedDateTime convertedTime = ZonedDateTime.parse(time, timeFormatter)
				.withZoneSameInstant(ZoneId.of(timezoneConfigurationProperties.getTimezone()));
		return convertedTime.format(DateTimeFormatter.ofPattern(timezoneConfigurationProperties.getOutputTimeFormat()));
	}
}
