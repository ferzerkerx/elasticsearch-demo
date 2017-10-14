package com.ferzerkerx.elasticsearch.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferzerkerx.elasticsearch.demo.model.EventData;

import javax.annotation.Nonnull;
import java.util.Optional;

public final class Util {

    private Util() {
    }

    @Nonnull
    public static Optional<String> toJson(@Nonnull EventData event) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Optional.of(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException ignored) {
        }
        return Optional.empty();
    }
}
