package io.switchbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TimezoneProcessorTest {

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private Processor processor;

    @SuppressWarnings("unchecked")
    @Test
    public void time_converted_to_timezone_successfully() {
        Message<String> message = new GenericMessage<>("11/08/16 11:36:39");

        this.processor.input().send(message);

        Message<String> received = (Message<String>) this.messageCollector.forChannel(this.processor.output()).poll();
        assertThat(received.getPayload()).isEqualTo("11/08/16 13:36:39");
    }
}
