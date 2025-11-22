package ua.profitsoft.observations.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import ua.profitsoft.observations.model.Observation;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class ObservationParser {

    private final JsonFactory jsonFactory = new JsonFactory();

    public void parseFile(Path file, Consumer<Observation> consumer) throws Exception {
        try (InputStream in = Files.newInputStream(file);
             JsonParser parser = jsonFactory.createParser(in)) {

            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected JSON array in file: " + file);
            }

            while (parser.nextToken() != JsonToken.END_ARRAY) {
                if (parser.currentToken() != JsonToken.START_OBJECT) {
                    parser.skipChildren();
                    continue;
                }

                String id = null;
                String author = null;
                long timestamp = 0L;
                String location = null;
                String tags = null;
                String instrument = null;

                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String fieldName = parser.getCurrentName();
                    parser.nextToken();

                    if ("id".equals(fieldName)) {
                        id = parser.getValueAsString();
                    } else if ("author".equals(fieldName)) {
                        author = parser.getValueAsString();
                    } else if ("timestamp".equals(fieldName) || "time".equals(fieldName)) {
                        timestamp = parser.getValueAsLong(0L);
                    } else if ("location".equals(fieldName)) {
                        location = parser.getValueAsString();
                    } else if ("tags".equals(fieldName)) {
                        tags = parser.getValueAsString();
                    } else if ("instrument".equals(fieldName)) {
                        instrument = parser.getValueAsString();
                    } else {
                        parser.skipChildren();
                    }
                }

                Observation obs = new Observation(id, author, timestamp, location, tags, instrument);
                consumer.accept(obs);
            }
        }
    }
}
