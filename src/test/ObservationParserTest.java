package ua.profitsoft.observations.parser;

import org.junit.jupiter.api.Test;
import ua.profitsoft.observations.model.Observation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ObservationParserTest {

    @Test
    void parseValidJsonMultipleObjects() throws Exception {
        Path path = Path.of(getClass().getResource("/sample.json").toURI());
        ObservationParser parser = new ObservationParser();
        List<Observation> list = new ArrayList<>();

        parser.parseFile(path, list::add);

        assertEquals(2, list.size());
        assertEquals("A", list.get(0).author());
    }

    @Test
    void parseWithEmptyTags() throws Exception {
        Path path = Path.of(getClass().getResource("/sample_empty_tags.json").toURI());
        ObservationParser parser = new ObservationParser();
        List<Observation> list = new ArrayList<>();

        parser.parseFile(path, list::add);

        assertEquals(1, list.size());
        assertEquals("", list.get(0).tags());
    }

    @Test
    void parseInvalidJsonShouldThrow() {
        Path path = Path.of("src/test/resources/sample_invalid.json");
        ObservationParser parser = new ObservationParser();
        List<Observation> list = new ArrayList<>();

        assertThrows(Exception.class, () -> parser.parseFile(path, list::add));
    }

    @Test
    void parseEmptyFileShouldReturnEmptyList() throws Exception {
        Path path = Path.of(getClass().getResource("/empty.json").toURI());
        ObservationParser parser = new ObservationParser();
        List<Observation> list = new ArrayList<>();

        parser.parseFile(path, list::add);

        assertTrue(list.isEmpty());
    }
}
