package ua.profitsoft.observations;

import ua.profitsoft.observations.processor.ObservationProcessor;
import ua.profitsoft.observations.statistics.StatisticsAggregator;
import ua.profitsoft.observations.xml.XmlWriter;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public class Main {

    private static final String USAGE = "Usage: java -jar observations-statistics.jar <dir> <attribute> [threads]\n" +
            "attribute: author | location | instrument | tags\n" +
            "threads: optional, default = 4";

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println(USAGE);
            return;
        }

        Path dir = Path.of(args[0]);
        String attribute = args[1];
        int threads = args.length >= 3 ? Integer.parseInt(args[2]) : 4;

        if (!Set.of("author", "location", "instrument", "tags").contains(attribute)) {
            System.out.println("Unknown attribute: " + attribute);
            System.out.println(USAGE);
            return;
        }

        StatisticsAggregator aggregator = new StatisticsAggregator();
        ObservationProcessor processor = new ObservationProcessor();

        long start = System.currentTimeMillis();
        processor.process(dir, attribute, threads, aggregator);
        long procTime = System.currentTimeMillis() - start;

        Map<String, Long> stats = aggregator.snapshot();

        XmlWriter writer = new XmlWriter();
        writer.write(stats, attribute);

        System.out.printf("Processed in %d ms using %d threads. Distinct values: %d%n",
                procTime, threads, stats.size());
    }
}
