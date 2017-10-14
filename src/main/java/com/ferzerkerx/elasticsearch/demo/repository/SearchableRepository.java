package com.ferzerkerx.elasticsearch.demo.repository;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public interface SearchableRepository<T, Q> {

    @Nonnull
    Stream<? extends T> findBy(@Nonnull Q query);
}
