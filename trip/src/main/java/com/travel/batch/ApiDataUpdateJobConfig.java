package com.travel.batch;

import com.travel.place.model.PlaceDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

@Configuration
@EnableBatchProcessing
public class ApiDataUpdateJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public ApiDataUpdateJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job apiDataUpdateJob(ItemReader<PlaceDto> reader,
                                ItemProcessor<PlaceDto, PlaceDto> processor,
                                ItemWriter<PlaceDto> writer) {
        return new JobBuilder("apiDataUpdateJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(apiDataUpdateStep(reader, processor, writer))
                .build();
    }

    @Bean
    public Step apiDataUpdateStep(ItemReader<PlaceDto> reader,
                                  ItemProcessor<PlaceDto, PlaceDto> processor,
                                  ItemWriter<PlaceDto> writer) {
        return new StepBuilder("apiDataUpdateStep", jobRepository)
                .<PlaceDto, PlaceDto>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public ItemReader<PlaceDto> apiDataReader() {
        return new ApiDataReader(new RestTemplate(), "API_URL"); // 실제 API URL로 변경
    }

    @Bean
    public ItemProcessor<PlaceDto, PlaceDto> apiDataProcessor() {
        return new ApiDataProcessor();
    }

    @Bean
    public ItemWriter<PlaceDto> apiDataWriter() {
        return new ApiDataWriter();
    }
}
