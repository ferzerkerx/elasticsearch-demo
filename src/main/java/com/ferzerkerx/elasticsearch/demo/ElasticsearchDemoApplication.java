package com.ferzerkerx.elasticsearch.demo;

import com.ferzerkerx.elasticsearch.demo.elasticsearch.ElasticSearchEventDataRepository;
import com.ferzerkerx.elasticsearch.demo.model.EventData;
import com.ferzerkerx.elasticsearch.demo.repository.EventDataQuery;
import com.ferzerkerx.elasticsearch.demo.repository.EventDataRepository;
import com.ferzerkerx.elasticsearch.demo.elasticsearch.ElasticSearchClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

@SpringBootApplication
@Import({
        AppConfiguration.class
})
public class ElasticsearchDemoApplication implements CommandLineRunner {

    private static final int NUMBER_OF_ENTRIES = 15;

    @Nonnull
    private final ElasticSearchClient elasticSearchClient;

    @Nonnull
    private final EventDataRepository eventDataRepository;

    @Nonnull
    private final ElasticSearchEventDataRepository elasticSearchEventDataRepository;

    public ElasticsearchDemoApplication(@Nonnull ElasticSearchClient elasticSearchClient,
                                        @Nonnull EventDataRepository eventDataRepository,
                                        @Nonnull ElasticSearchEventDataRepository elasticSearchEventDataRepository) {
        this.elasticSearchClient = elasticSearchClient;
        this.eventDataRepository = eventDataRepository;
        this.elasticSearchEventDataRepository = elasticSearchEventDataRepository;
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ElasticsearchDemoApplication.class);
        try (final ConfigurableApplicationContext context = springApplication.run(args)) {

        }
    }

    @Override
    public void run(String... strings) throws Exception {
        try (Stream<? extends EventData> events = findEvents()) {
            events.map(Util::toJson)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(this::push);
        }

        try (Stream<? extends EventData> eventsInEs = findEventsInEs()) {
            eventsInEs.forEach(System.out::println);
        }

    }

    private void push(@Nonnull String json) {
        elasticSearchClient.postJson(json, LocalDate.now());
    }

    @Nonnull
    private Stream<? extends EventData> findEvents() {
        EventDataQuery eventDataQuery = EventDataQuery.builder()
                .withMaxNumberOfEntries(NUMBER_OF_ENTRIES)
                .build();

        return eventDataRepository.findBy(eventDataQuery);
    }

    @Nonnull
    private Stream<? extends EventData> findEventsInEs() {
        EventDataQuery eventDataQuery = EventDataQuery.builder()
                .withMaxNumberOfEntries(NUMBER_OF_ENTRIES)
                .build();

        return elasticSearchEventDataRepository.findBy(eventDataQuery);
    }


}
