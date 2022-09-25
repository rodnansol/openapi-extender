package org.rodnansol.core.resource;

import org.rodnansol.core.example.ExampleReference;
import org.rodnansol.core.example.ExampleReferenceType;
import org.rodnansol.core.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * File name based resource reader.
 * <p>
 * Files names are containing all the necessary information about the example reference.
 */
public class FileNameBasedExampleResourceReader implements ExampleResourceReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileNameBasedExampleResourceReader.class);

    /**
     * Default file name pattern.
     * <p>
     * Groups:
     * <ol>
     *     <li>OperationId</li>
     *     <li>Status Code</li>
     *     <li>Key/name of the example</li>
     *     <li>__</li>
     *     <li>Optional description</li>
     *     <li>File extension</li>
     * </ol>
     */
    private static final String DEFAULT_FILE_NAME_PATTERN = "(.*)_(\\d+)_([^_]*)(__)?(.*).(json|xml|yaml|yml)";
    private static final int OPERATION_ID_GROUP_INDEX = 1;
    private static final int STATUS_CODE_GROUP_INDEX = 2;
    private static final int EXTENSION_GROUP_INDEX = 6;
    private static final int KEY_GROUP_INDEX = 3;
    private static final int OPTIONAL_DESCRIPTION_GROUP_INDEX = 5;

    @Override
    public ExampleReferenceCreationResult createExampleReferences(ExampleReferenceType type, ExtenderResource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("resource is NULL");
        }
        LOGGER.debug("Starting to read resource with name[{}]", resource.getName());
        String content = getContent(resource);
        return createExampleReferenceByFileName(type, resource.getName())
            .map(exampleReference -> {
                exampleReference.setContent(content);
                return new ExampleReferenceCreationResult(exampleReference.getOperationId(), Collections.singletonList(exampleReference));
            })
            .orElse(null);
    }

    private String getContent(ExtenderResource resource) {
        try {
            return IOUtils.readStreamContent(resource.getContent());
        } catch (IOException e) {
            String message = "Error during reading the content of the file:[" + resource.getName() + "]";
            throw new ExampleResourceReaderException(message, e);
        }
    }

    private Optional<ExampleReference> createExampleReferenceByFileName(ExampleReferenceType type, String fileName) {
        Matcher matcher = Pattern.compile(DEFAULT_FILE_NAME_PATTERN).matcher(fileName);
        if (!matcher.matches()) {
            return Optional.empty();
        } else {
            return Optional.of(ExampleReference.builder()
                .operationId(matcher.group(OPERATION_ID_GROUP_INDEX))
                .statusCode(matcher.group(STATUS_CODE_GROUP_INDEX))
                .name(matcher.group(KEY_GROUP_INDEX))
                .mediaType(IOUtils.MEDIA_TYPE.get(matcher.group(EXTENSION_GROUP_INDEX)))
                .description(matcher.group(OPTIONAL_DESCRIPTION_GROUP_INDEX))
                .type(type)
                .build());
        }
    }
}
