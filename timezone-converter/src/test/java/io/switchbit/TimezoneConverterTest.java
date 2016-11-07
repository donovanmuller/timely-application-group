package io.switchbit;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimezoneConverterTest {

    @Test
    public void convert_timezone_successfully() {
        TimezoneConfigurationProperties timezoneConfigurationProperties = new TimezoneConfigurationProperties();
        timezoneConfigurationProperties.setTimezone("Africa/Johannesburg");

        String convertedTime = new TimezoneConverter(timezoneConfigurationProperties).convert("11/09/16 10:30:00");

        // Timezone of Africa/Johannesburg (SAST) is UTC+02:00
        assertThat(convertedTime).isEqualTo("11/09/16 12:30:00");
    }
}
