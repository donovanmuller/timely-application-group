package io.switchbit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
		Application.class,
		TimezoneJobConfigurationTest.JobTestConfiguration.class
})
public class TimezoneJobConfigurationTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Before
	public void cleanup() {
		new FileSystemResource("target/times-out").getFile().delete();
	}

	@Test
	public void convert_time_batch_successfully() throws Exception {
		HashMap<String, JobParameter> parameters = new HashMap<>();
		parameters.put("inputFile", new JobParameter(new ClassPathResource("times-in").getFile().getAbsolutePath()));
		parameters.put("timezone", new JobParameter("Africa/Johannesburg"));
		parameters.put("outputFile", new JobParameter("target/times-out"));

		BatchStatus status = jobLauncherTestUtils.launchJob(new JobParameters(parameters)).getStatus();

		assertThat(status).isEqualTo(BatchStatus.COMPLETED);
		assertThat(new FileSystemResource("target/times-out").getFile())
				.hasSameContentAs(new ClassPathResource("times-out-check").getFile());
	}

	@TestConfiguration
	static class JobTestConfiguration {

		@Bean
		public JobLauncherTestUtils jobLauncherTestUtils() {
			return new JobLauncherTestUtils();
		}
	}
}
