package io.switchbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;

@EnableBinding(Processor.class)
public class TimezoneProcessor {

	private static final Logger log = LoggerFactory.getLogger(TimezoneProcessor.class);

	private TimezoneConverter timezoneConverter;

	public TimezoneProcessor(final TimezoneConverter timezoneConverter) {
		this.timezoneConverter = timezoneConverter;
	}

	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	String convertTimezone(String time) {
		return timezoneConverter.convert(time);
	}
}
