package ua.profitsoft.observations.statistics;

import org.junit.jupiter.api.Test;
import ua.profitsoft.observations.model.Observation;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StatisticsAggregatorTest {

    @Test
    void testTagsAggregationBasic() {
        StatisticsAggregator ag = new StatisticsAggregator();
        ag.accept(new Observation("1","A",0,"L","moon, jupiter","I1"), "tags");
        ag.accept(new Observation("2","B",0,"L","moon","I2"), "tags");

        Map<String, Long> snapshot = ag.snapshot();
        assertEquals(2L, snapshot.get("moon"));
        assertEquals(1L, snapshot.get("jupiter"));
    }

    @Test
    void testAuthorAggregation() {
        StatisticsAggregator ag = new StatisticsAggregator();
        ag.accept(new Observation("1","Alice",0,"L","","I1"), "author");
        ag.accept(new Observation("2","Alice",0,"L","","I2"), "author");
        ag.accept(new Observation("3","Bob",0,"L","","I2"), "author");

        Map<String, Long> sn = ag.snapshot();
        assertEquals(2L, sn.get("Alice"));
        assertEquals(1L, sn.get("Bob"));
    }

    @Test
    void testTagsWithExtraSpaces() {
        StatisticsAggregator ag = new StatisticsAggregator();
        ag.accept(new Observation("1","A",0,"L","moon ,  mars ,moon","I"), "tags");

        Map<String, Long> sn = ag.snapshot();
        assertEquals(2L, sn.get("moon"));
        assertEquals(1L, sn.get("mars"));
    }

    @Test
    void testEmptyTagsIgnored() {
        StatisticsAggregator ag = new StatisticsAggregator();
        ag.accept(new Observation("1","A",0,"L","","I"), "tags");

        Map<String, Long> sn = ag.snapshot();
        assertTrue(sn.isEmpty());
    }

    @Test
    void testNullFieldShouldNotThrowButIgnore() {
        StatisticsAggregator ag = new StatisticsAggregator();
        ag.accept(new Observation("1","A",0,"L","moon","I"), null);

        Map<String, Long> sn = ag.snapshot();
        assertTrue(sn.isEmpty());
    }
}
