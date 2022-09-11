package org.rodnansol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractFileOutputResourceGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileOutputResourceGenerator.class);

    /**
     * Generates resources based on the incoming object.
     * <p>
     * Files are going to be created based on the operation and its dependencies.
     *
     * @param params parameter object with all necessary information.
     * @throws IOException when resource/file generation fails.
     */
    void generateResources(ResourceGeneratorParams params) throws IOException {
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
        FileWriter.INSTANCE.writeToFile(file, content, contentType);
    }

    protected abstract String createAggregatorFile(String operation, String description, int status, String contentType);

    protected abstract String getFolderName(String operation);

}
