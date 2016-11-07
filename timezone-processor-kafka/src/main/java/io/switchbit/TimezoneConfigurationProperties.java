package io.switchbit;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class TimezoneConfigurationProperties {

    private String timezone;
    private String inputTimeFormat = "MM/dd/yy HH:mm:ss";
    private String outputTimeFormat = "MM/dd/yy HH:mm:ss";

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getInputTimeFormat() {
        return inputTimeFormat;
    }

    public void setInputTimeFormat(final String inputTimeFormat) {
        this.inputTimeFormat = inputTimeFormat;
    }

    public String getOutputTimeFormat() {
        return outputTimeFormat;
    }

    public void setOutputTimeFormat(final String outputTimeFormat) {
        this.outputTimeFormat = outputTimeFormat;
    }
}
