package co.rodnan;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class DocumentReporter implements ResultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentReporter.class);

    private static final Map<String, String> MEDIA_TYPE;

    static {
        MEDIA_TYPE = new HashMap<>();
        MEDIA_TYPE.put("application/json", ".json");
        MEDIA_TYPE.put("application/xml", ".xml");
    }

    private final String operation;
    private final String description;

    public DocumentReporter(String operation, String description) {
        this.operation = operation;
        this.description = description;
    }

    @Override
    public void handle(MvcResult result) throws Exception {
        try {
            MockHttpServletResponse response = result.getResponse();
            String contentType = response.getContentType();
            String content = response.getContentAsString(StandardCharsets.UTF_8);
            if (content == null || content.equals("")) {
                LOGGER.debug("Content is empty, no file is going to be generated");
                return;
            }
            int status = response.getStatus();
            String folderName = getFolderName(operation);
            File folder = Paths.get(folderName).toFile();
            folder.mkdirs();
            String aggregatorFile = createAggregatorFile(operation, description, status, contentType);
            ObjectMapper mapper = new ObjectMapper();
            Path file = Paths.get(aggregatorFile);
            if (!file.toFile().exists()) {
                file = Files.createFile(file);
            }
            Object object = mapper.readValue(content, Object.class);
            String prettyContent = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            Files.write(file, prettyContent.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String createAggregatorFile(String operation, String description, int status, String contentType) {
        return getFolderName(operation) + status + "_" + description + MEDIA_TYPE.get(contentType);
    }

    private String getFolderName(String operation) {
        return "target/classes/openapi/" + operation + "/";
    }
}
