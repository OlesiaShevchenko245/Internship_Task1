package ua.profitsoft.observations.parser;

import org.junit.jupiter.api.Test;
import ua.profitsoft.observations.model.Observation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObservationParserTest {
 
    @Test
    void parseSampleJson() throws Exception {
        Path path = Path.of(getClass().getResource("/sample.json").toURI());
        ObservationParser parser = new ObservationParser();
        List<Observation> list = new ArrayList<>();
        parser.parseFile(path, list::add);
        assertEquals(2, list.size());
        assertEquals("A", list.get(0).author());
        assertEquals("comet", list.get(1).tags());
    }
}
