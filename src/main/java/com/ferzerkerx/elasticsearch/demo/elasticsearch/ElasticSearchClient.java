package com.ferzerkerx.elasticsearch.demo.elasticsearch;


import com.jayway.jsonpath.DocumentContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import static com.ferzerkerx.elasticsearch.demo.elasticsearch.Util.getEndpoint;
import static com.ferzerkerx.elasticsearch.demo.elasticsearch.Util.parse;
import static com.ferzerkerx.elasticsearch.demo.elasticsearch.Util.toBodyEntity;
import static com.ferzerkerx.elasticsearch.demo.elasticsearch.Util.toScrollDeleteBodyEntity;
import static java.util.Collections.emptyMap;

@Component
public final class ElasticSearchClient implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchClient.class);
    private static final int MAX_RETRIES = 3;

    @Nonnull
    private final RestClient client;

    @Autowired
    public ElasticSearchClient() {
        HttpHost http = new HttpHost("localhost", 9200, "http");
        client = RestClient.builder(http).build();
    }

    public void postJson(@Nonnull String jsonContent, @Nonnull LocalDate date) {

        String endpoint = getEndpoint(date);

        try (NStringEntity entity = new NStringEntity(jsonContent, ContentType.APPLICATION_JSON)){
            int retries = 0;
            while (retries < MAX_RETRIES) {
                try {
                    client.performRequest("POST", endpoint, Collections.emptyMap(), entity);
                    break;
                } catch (IOException e) {
                    LOG.warn("Failed to push to {} will retry... {}" , endpoint, retries);
                    retries++;
                }
            }

            if (retries >= MAX_RETRIES) {
                throw new ElasticSearchClientException("Failed to push to ElasticSearch" + jsonContent);
            }
        }

    }

    public void deleteScroll(String scrollId) {
        try {
            HttpEntity entity = toScrollDeleteBodyEntity(scrollId);
            client.performRequest("DELETE", "/_search/scroll", emptyMap(), entity);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }



    @Override
    public void close() throws Exception {
        client.close();
    }

    public DocumentContext doSearch(@Nonnull Map<String, String> parameters, @Nonnull String query) {
        HttpEntity entity = toBodyEntity(query);
        return doSearch(parameters, entity);
    }

    public DocumentContext doSearch(@Nonnull Map<String, String> parameters, @Nonnull HttpEntity entity) {
        String endpoint = getEndpoint(LocalDate.now()) + "_search";
        try {
            Response response = client.performRequest("POST", endpoint, parameters, entity);
            return parse(response);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private class ElasticSearchClientException extends RuntimeException {
        ElasticSearchClientException(String message) {
            super(message);
        }
    }
}
