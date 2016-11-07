package io.switchbit;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.FormatterLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * A Spring Batch Job (<code>timezone-task</code>) configuration to:
 *
 * <ol>
 *     <li>Read a file specified by the <code>inputFile</code> job parameter</li>
 *     <li>Process each line (representing a time) of the file by converting each time to the timezone specified
 *     by the <code>timezone</code> job parameter</li>
 *     <li>Write the converted times to a file specified by the <code>outputFile</code> job parameter</li>
 * </ol>
 */
@Configuration
public class TimezoneJobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job(Step timezone) throws Exception {
        return jobBuilderFactory.get("timezone-task")
                .incrementer(new RunIdIncrementer())
                .start(timezone)
                .build();
    }

    @Bean
    public Step timezoneConversion(FlatFileItemReader<TimelyTime> reader,
            ItemProcessor<TimelyTime, TimelyTime> itemProcessor,
            ItemWriter<TimelyTime> writer) {
        return stepBuilderFactory.get("timezone-conversion")
                .<TimelyTime, TimelyTime>chunk(10)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<TimelyTime> reader(@Value("#{jobParameters[inputFile]}") String inputFile) {
        FlatFileItemReader<TimelyTime> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(inputFile));
        reader.setLineMapper((line, lineNumber) -> new TimelyTime(line));
        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<TimelyTime, TimelyTime> timezoneProcessor(TimezoneConverter timezoneConverter,
            @Value("#{jobParameters[timezone]}") String timezone) {
        return new ItemProcessor<TimelyTime, TimelyTime>() {

            @Override
            public TimelyTime process(final TimelyTime timelyTime) throws Exception {
                return timelyTime.converted(
                        timezoneConverter.convert(timelyTime.getOriginalTime()),
                        timezone);
            }
        };
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<TimelyTime> writer(@Value("#{jobParameters[outputFile]}") String outputFile) {
        FlatFileItemWriter<TimelyTime> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(outputFile));

        FormatterLineAggregator<TimelyTime> lineAggregator = new FormatterLineAggregator<>();
        BeanWrapperFieldExtractor<TimelyTime> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"originalTime", "convertedTime", "timezone"});
        lineAggregator.setFieldExtractor(fieldExtractor);
        lineAggregator.setFormat("%s [UTC] -> %s [%s]");
        writer.setLineAggregator(lineAggregator);
        return writer;
    }

    @Bean
    @StepScope
    public TimezoneConverter timezoneConverter(@Value("#{jobParameters[timezone]}") String timezone) {
        TimezoneConfigurationProperties timezoneConfigurationProperties = new TimezoneConfigurationProperties();
        timezoneConfigurationProperties.setTimezone(timezone);
        return new TimezoneConverter(timezoneConfigurationProperties);
    }
}
