package io.switchbit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Thanks to @websightgmbh for https://github.com/cedricziel/demo-sse-spring-boot
 * which this SSE controller is based on.
 */
@Controller
public class TimelyEventsController {

    private final List<ResponseBodyEmitter> emitters = new ArrayList<>();

    private TimelyEventConfigurationProperties timelyEventConfigurationProperties;

    public TimelyEventsController(TimelyEventConfigurationProperties timelyEventConfigurationProperties) {
        this.timelyEventConfigurationProperties = timelyEventConfigurationProperties;
    }

    @GetMapping("/events/subscribe")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(timelyEventConfigurationProperties.getSseTimeout());
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        return emitter;
    }

    public void publish(String event) {
        emitters.forEach(emitter -> {
            try {
                emitter.send(event, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        });
    }
}
