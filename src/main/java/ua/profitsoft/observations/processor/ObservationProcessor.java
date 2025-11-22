package ua.profitsoft.observations.processor;

import ua.profitsoft.observations.parser.ObservationParser;
import ua.profitsoft.observations.statistics.StatisticsAggregator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ObservationProcessor {

    private final ObservationParser parser = new ObservationParser();

    public void process(Path dirPath, String attribute, int threads, StatisticsAggregator aggregator) throws Exception {
        if (!Files.isDirectory(dirPath)) {
            throw new IllegalArgumentException("Path is not a directory: " + dirPath);
        }

        List<Path> files = Files.list(dirPath)
                .filter(p -> p.toString().toLowerCase().endsWith(".json"))
                .collect(Collectors.toList());

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CompletionService<Void> cs = new ExecutorCompletionService<>(executor);

        for (Path file : files) {
            cs.submit(() -> {
                parser.parseFile(file, obs -> aggregator.accept(obs, attribute));
                return null;
            });
        }

        for (int i = 0; i < files.size(); i++) {
            Future<Void> f = cs.take();
            try {
                f.get();
            } catch (ExecutionException ex) {
                throw new RuntimeException("Error processing file", ex.getCause());
            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}
