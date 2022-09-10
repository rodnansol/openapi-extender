package co.rodnansol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileWriter {

    public static final FileWriter INSTANCE = new FileWriter();
    private static final XmlMapper XML_MAPPER = new XmlMapper();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private FileWriter() {
    }

    public void writeToFile(Path file, byte[] content, String contentType) throws IOException {
        String prettyContent;
        if (contentType.contains("json")) {
            Object object = OBJECT_MAPPER.readValue(content, Object.class);
            prettyContent = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } else if (contentType.contains("xml")) {
            Object object = XML_MAPPER.readValue(content, Object.class);
            prettyContent = XML_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } else {
            throw new RuntimeException("Nem jó");
        }
        Files.write(file, prettyContent.getBytes());
    }

}
