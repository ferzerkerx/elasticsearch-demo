package com.ferzerkerx.elasticsearch.demo.repository;

import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Optional;

@Immutable
public class EventDataQuery {

    @Nonnegative
    private final long maxNumberOfEntries;

    public EventDataQuery(@Nonnegative long maxNumberOfEntries) {
        this.maxNumberOfEntries = maxNumberOfEntries;
    }

    @Nonnegative
    public long getMaxNumberOfEntries() {
        return maxNumberOfEntries;
    }

    @Nonnull
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private static final int DEFAULT_NUMBER_OF_ENTRIES = 30;

        private Optional<Long> numberOfEntries;

        private Builder() {
        }

        public Builder withMaxNumberOfEntries(@Nonnegative long maxNumberOfEntries) {
            this.numberOfEntries = Optional.of(maxNumberOfEntries);
            return this;
        }

        public EventDataQuery build() {
            return new EventDataQuery(numberOfEntries.orElse((long) DEFAULT_NUMBER_OF_ENTRIES));
        }
    }
}
