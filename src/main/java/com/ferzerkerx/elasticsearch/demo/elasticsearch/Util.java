package com.ferzerkerx.elasticsearch.demo.elasticsearch;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

public final class Util {

    private Util() {
    }

    @Nonnull
    protected static String indexName(@Nonnull LocalDate date) {
        return "index-" + date.getYear() + "." + date.getMonthValue() + "." + date.getDayOfMonth();
    }

    @Nonnull
    protected static String getEndpoint(@Nonnull LocalDate date) {
        return "/" + indexName(date) + "/test/";
    }

    @Nonnull
    protected static HttpEntity toScrollDeleteBodyEntity(@Nonnull String scrollId) {
        return toBodyEntity("{" +
                "\"scroll_id\": \"" + scrollId + "\"" +
                "}"
        );
    }

    @Nonnull
    protected static HttpEntity toScrollContinueBodyEntity(@Nonnull String scrollId) {
        return toBodyEntity("{" +
                "\"scroll\": \"1d\"," +
                "\"scroll_id\": \"" + scrollId + "\"" +
                "}"
        );
    }

    @Nonnull
    protected static HttpEntity toBodyEntity(@Nonnull String query) {
        return new NStringEntity(query, ContentType.APPLICATION_JSON);
    }

    @Nonnull
    protected static DocumentContext parse(@Nonnull Response response) throws IOException {
        try (final InputStream is = response.getEntity().getContent()) {
            return JsonPath.parse(is);
        }
    }
}
