package org.rodnansol.generator;

import org.rodnansol.generator.io.FileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Abstract class that generates the resource files
 */
public abstract class AbstractFileOutputResourceGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileOutputResourceGenerator.class);

    protected final String outputDirectory;

    protected AbstractFileOutputResourceGenerator(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Generates resources based on the incoming object.
     * <p>
     * Files are going to be created based on the operation and its dependencies.
     *
     * @param params parameter object with all necessary information.
     * @throws IOException when resource/file generation fails.
     */
    public void generateResources(ResourceGeneratorParams params) throws IOException {
        String operation = params.getOperation();
        String contentType = params.getContentType();
        byte[] content = params.getContent();
        if (content == null || content.length == 0) {
            LOGGER.debug("Content is empty, no file is going to be generated");
            return;
        }
        int status = params.getStatus();
        createFolderIfNeeded();
        Path file = getFile(operation, status, contentType, params.getName(), params.getDescription());
        FileWriter.INSTANCE.writeToFile(file, content, contentType);
    }


    /**
     * Returns the file path/name based on the incoming parameters.
     *
     * @return created file path/name.
     */
    protected abstract String createFilePath(String operation, int status, String contentType, String name, String optionalDescription);

    /**
     * Returns the containing folder.
     *
     * @param operation operation's name.
     * @return resource containing folder name.
     */
    protected String getFolderName() {
        return outputDirectory + "/";
    }

    private void createFolderIfNeeded() {
        String folderName = getFolderName();
        File folder = Paths.get(folderName).toFile();
        folder.mkdirs();
    }

    private Path getFile(String operation, int status, String contentType, String name, String optionalDescription) throws IOException {
        String aggregatorFile = createFilePath(operation, status, contentType, name, optionalDescription);
        Path file = Paths.get(aggregatorFile);
        if (!file.toFile().exists()) {
            LOGGER.debug("File does not exist, creating it:[{}]", aggregatorFile);
            file = Files.createFile(file);
        }
        return file;
    }

}
