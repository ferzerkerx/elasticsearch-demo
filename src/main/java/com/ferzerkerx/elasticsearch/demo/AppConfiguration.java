package com.ferzerkerx.elasticsearch.demo;

import com.ferzerkerx.elasticsearch.demo.elasticsearch.ElasticSearchEventDataRepository;
import com.ferzerkerx.elasticsearch.demo.elasticsearch.ElasticSearchEventDataRepositoryRepositoryImpl;
import com.ferzerkerx.elasticsearch.demo.repository.EventDataRepository;
import com.ferzerkerx.elasticsearch.demo.repository.FakeEventDataRepositoryImpl;
import com.ferzerkerx.elasticsearch.demo.elasticsearch.ElasticSearchClient;
import org.springframework.context.annotation.Bean;

public class AppConfiguration {

    @Bean
    public ElasticSearchClient elasticSearchClient() {
        return new ElasticSearchClient();
    }

    @Bean
    public EventDataRepository eventDataRepository() {
        return new FakeEventDataRepositoryImpl();
    }

    @Bean
    public ElasticSearchEventDataRepository elasticSearchEventDataRepository(ElasticSearchClient elasticSearchClient) {
        return new ElasticSearchEventDataRepositoryRepositoryImpl(elasticSearchClient);
    }
}
