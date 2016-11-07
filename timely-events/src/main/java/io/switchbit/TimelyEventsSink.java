package io.switchbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@EnableBinding(Sink.class)
public class TimelyEventsSink {

    private static final Logger log = LoggerFactory.getLogger(TimelyEventsSink.class);

    private TimelyEventsController publisher;

    public TimelyEventsSink(TimelyEventsController publisher) {
        this.publisher = publisher;
    }

    @StreamListener(Sink.INPUT)
    void consumeEvent(String event) {
        log.info("Consuming event: '{}'", event);
        publisher.publish(event);
    }
}
