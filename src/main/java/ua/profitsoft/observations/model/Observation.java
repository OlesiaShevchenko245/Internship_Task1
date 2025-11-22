package ua.profitsoft.observations.model;

public record Observation(
        String id,
        String author,
        long timestamp,
        String location,
        String tags,
        String instrument
) {}
