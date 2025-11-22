package ua.profitsoft.observations.statistics;

import ua.profitsoft.observations.model.Observation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class StatisticsAggregator {

    private final ConcurrentHashMap<String, LongAdder> counts = new ConcurrentHashMap<>();

    public void accept(Observation obs, String attribute) {
        if (obs == null) return;

        switch (attribute) {
            case "author" -> incrementSafe(obs.author());
            case "location" -> incrementSafe(obs.location());
            case "instrument" -> incrementSafe(obs.instrument());
            case "tags" -> {
                String tags = obs.tags();
                if (tags != null && !tags.isBlank()) {
                    String[] parts = tags.split(",");
                    for (String raw : parts) {
                        String tag = raw.trim();
                        if (!tag.isEmpty()) incrementSafe(tag);
                    }
                }
            }
            default -> throw new IllegalArgumentException("Unknown attribute: " + attribute);
        }
    }

    private void incrementSafe(String key) {
        if (key == null) key = "<null>";
        counts.computeIfAbsent(key, k -> new LongAdder()).increment();
    }

    public Map<String, Long> snapshot() {
        return counts.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().longValue()));
    }
}
