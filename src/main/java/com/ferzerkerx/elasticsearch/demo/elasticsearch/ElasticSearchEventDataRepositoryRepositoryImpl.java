package com.ferzerkerx.elasticsearch.demo.elasticsearch;

import com.ferzerkerx.elasticsearch.demo.model.EventData;
import com.ferzerkerx.elasticsearch.demo.repository.EventDataQuery;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static com.ferzerkerx.elasticsearch.demo.elasticsearch.Util.toScrollContinueBodyEntity;
import static java.util.Collections.emptyMap;

@Repository
public class ElasticSearchEventDataRepositoryRepositoryImpl implements ElasticSearchEventDataRepository {

    @Nonnull
    private final ElasticSearchClient elasticSearchClient;

    @Autowired
    public ElasticSearchEventDataRepositoryRepositoryImpl(ElasticSearchClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    @Override
    @Nonnull
    public Stream<EventData> findBy(@Nonnull EventDataQuery query) {
        final AtomicReference<DocumentContext> currentContext = new AtomicReference<>();
        return Stream.generate(() -> findResultsInEs(currentContext))
                .onClose(() -> clearScrollIfRequired(currentContext.get()))
                .flatMap(List::stream)
                .map(JsonPath::parse)
                .map(this::fromJson)
                .limit(query.getMaxNumberOfEntries());
    }

    private List<Object> findResultsInEs(AtomicReference<DocumentContext> currentContext) {
        final DocumentContext context = currentContext.accumulateAndGet(null, (current, ignored) -> executeNextScroll(current));
        if (context == null) {
            return Collections.emptyList();
        }
        // noinspection unchecked
        final List<Object> hits = context.read("$['hits']['hits']", List.class);
        if (hits == null || hits.isEmpty()) {
            return Collections.emptyList();
        }
        return hits;
    }

    @Nullable
    protected DocumentContext executeNextScroll(@Nullable DocumentContext current) {
        if (current == null) {
            final Map<String, String> parameters = new HashMap<>();
            parameters.put("size", "10");
            parameters.put("scroll", "1d");
            return elasticSearchClient.doSearch(parameters, "");
        }
        final String scrollId = current.read("['_scroll_id']");
        if (scrollId == null) {
            return null;
        }
        if (current.jsonString().contains(scrollId)) {
            return null;
        }
        HttpEntity entity = toScrollContinueBodyEntity(scrollId);
        return elasticSearchClient.doSearch(emptyMap(), entity);
    }


    @Nonnull
    private EventData fromJson(@Nonnull DocumentContext documentContext) {
        long timestamp = documentContext.read("['_source']['timeStamp']", Long.class);
        double amount = documentContext.read("['_source']['amount']", Double.class);
        return new EventData(amount, timestamp);
    }

    protected void clearScrollIfRequired(@Nullable DocumentContext current) {
        if (current != null) {
            String scrollId = current.read("['_scroll_id']");
            if (scrollId != null) {
                elasticSearchClient.deleteScroll(scrollId);
            }
        }
    }


}