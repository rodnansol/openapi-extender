package co.rodnansol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

class ApiResponseExampleDocumenter {

    private static final Map<String, String> MEDIA_TYPE;

    static {
        MEDIA_TYPE = new HashMap<>();
        MEDIA_TYPE.put("application/json", ".json");
        MEDIA_TYPE.put("application/xml", ".xml");
    }

    private static final String OPENAPI_EXTENDER_RESPONSES_FOLDER = "openapi-extender/responses/";

    static final String DEFAULT_OUTPUT_DIRECTORY = "target/classes/" + OPENAPI_EXTENDER_RESPONSES_FOLDER;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseExampleDocumenter.class);

    private final String outputDirectory;

    public ApiResponseExampleDocumenter() {
        this(DEFAULT_OUTPUT_DIRECTORY);
    }

    public ApiResponseExampleDocumenter(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    void document(Params params) throws IOException {
        String operation = params.getOperation();
        String contentType = params.getContentType();
        byte[] content = params.getContent();
        if (content == null || content.length == 0) {
            LOGGER.debug("Content is empty, no file is going to be generated");
            return;
        }
        int status = params.getStatus();
        String folderName = getFolderName(operation);
        File folder = Paths.get(folderName).toFile();
        folder.mkdirs();
        String aggregatorFile = createAggregatorFile(operation, params.getDescription(), status, contentType);
        Path file = Paths.get(aggregatorFile);
        if (!file.toFile().exists()) {
            file = Files.createFile(file);
        }
        FileWriter.INSTANCE.writeToFile(file,content,contentType);
    }

    private String createAggregatorFile(String operation, String description, int status, String contentType) {
        return getFolderName(operation) + status + "_" + description + MEDIA_TYPE.get(contentType);
    }

    private String getFolderName(String operation) {
        return outputDirectory + operation + "/";
    }

    static class Params {

        private final String operation;
        private final String description;
        private final int status;
        private final String contentType;
        private final byte[] content;

        public Params(String operation, String description, int status, String contentType, byte[] content) {
            this.operation = operation;
            this.description = description;
            this.status = status;
            this.contentType = contentType;
            this.content = content;
        }

        public String getOperation() {
            return operation;
        }

        public String getDescription() {
            return description;
        }

        public int getStatus() {
            return status;
        }

        public String getContentType() {
            return contentType;
        }

        public byte[] getContent() {
            return content;
        }
    }

}
