package ua.profitsoft.observations.xml;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileWriter;
import java.util.Comparator;
import java.util.Map;

public class XmlWriter {

    public void write(Map<String, Long> stats, String attribute) throws Exception {
        var sorted = stats.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .toList();

        String fileName = "statistics_by_" + attribute + ".xml";
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try (FileWriter fw = new FileWriter(fileName)) {
            XMLStreamWriter writer = factory.createXMLStreamWriter(fw);
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("statistics");

            for (var entry : sorted) {
                writer.writeStartElement("item");

                writer.writeStartElement("value");
                writer.writeCharacters(entry.getKey());
                writer.writeEndElement();

                writer.writeStartElement("count");
                writer.writeCharacters(String.valueOf(entry.getValue()));
                writer.writeEndElement();

                writer.writeEndElement();
            }

            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        }
        System.out.println("Wrote statistics to " + fileName);
    }
}
