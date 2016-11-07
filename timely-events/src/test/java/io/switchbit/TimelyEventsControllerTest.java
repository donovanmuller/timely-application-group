package io.switchbit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TimelyEventsControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private Sink sink;

    @Test
    public void subscribe_to_and_receive_events_successfully() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        new Thread(() -> {
            String event = template.getForObject("/events/subscribe", String.class);
            assertThat(event.trim()).isEqualTo("data:Test");
            countDownLatch.countDown();
        }).start();

        // give the event subscription (GET /events/subscribe) some time to establish
        Thread.sleep(500L);

        sink.input().send(new GenericMessage<>("Test"));

        assertThat(countDownLatch.await(2, TimeUnit.SECONDS)).isTrue();
    }
}
