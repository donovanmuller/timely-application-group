package io.switchbit;

import lombok.Data;

@Data
public class TimelyTime {

    private String originalTime;
	private String convertedTime;
	private String timezone;

	public TimelyTime(String originalTime) {
		this.originalTime = originalTime;
	}

    public TimelyTime converted(String convertedTime, String timezone) {
        this.convertedTime = convertedTime;
        this.timezone = timezone;

        return this;
    }
}
