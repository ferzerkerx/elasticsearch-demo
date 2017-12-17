package com.ferzerkerx.elasticsearch.demo.repository;

import com.ferzerkerx.elasticsearch.demo.model.EventData;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

@Repository
public class FakeEventDataRepositoryImpl implements EventDataRepository {

    @Nonnull
    @Override
    public Stream<EventData> findBy(@Nonnull EventDataQuery query) {
        return Stream.generate(this::createEventData)
                .limit(query.getMaxNumberOfEntries());
    }

    @Nonnull
    private EventData createEventData() {
        double amount = Math.random() * 1000;
        return new EventData(amount);
    }
}
