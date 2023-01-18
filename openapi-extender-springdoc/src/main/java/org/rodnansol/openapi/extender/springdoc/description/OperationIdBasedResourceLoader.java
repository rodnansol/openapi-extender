package org.rodnansol.openapi.extender.springdoc.description;

import org.springframework.util.Assert;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author nandorholozsnyak
 * @since 0.3.1
 */
class OperationIdBasedResourceLoader extends AbstractResourceLoader implements ResourceLoader {

    private final String resourcePostfix;
    private final DescriptorType descriptorType;
    private final Function<LoadResourceCommand, Stream<String>> pathResolverFunction;

    OperationIdBasedResourceLoader(String resourcePostfix,
                                   DescriptorType descriptorType) {
        this.resourcePostfix = resourcePostfix;
        this.descriptorType = descriptorType;
        this.pathResolverFunction = getDefaultPathResolverFunction();
    }

    OperationIdBasedResourceLoader(String resourcePostfix,
                                   DescriptorType descriptorType,
                                   Function<LoadResourceCommand, Stream<String>> pathResolverFunction) {
        this.resourcePostfix = resourcePostfix;
        this.descriptorType = descriptorType;
        this.pathResolverFunction = pathResolverFunction;
    }

    @Override
    public byte[] loadResource(LoadResourceCommand loadResourceCommand) {
        Assert.notNull(loadResourceCommand, "operation is NULL");
        Assert.notNull(loadResourceCommand.getOperation(), "operation is NULL");

        return pathResolverFunction.apply(loadResourceCommand)
            .map(this::loadResourceByPath)
            .filter(bytes -> bytes != null && bytes.length > 0)
            .findFirst()
            .orElse(new byte[0]);
    }

    private Function<LoadResourceCommand, Stream<String>> getDefaultPathResolverFunction() {
        //1. /<resourceBasePath>/<operationId><postFix><extension>
        //2. /<resourceBasePath>/<operationId>/<descriptorType><extension>
        return command -> Stream.of(
            String.format("/%s/%s%s%s", command.getResourceBasePath(), command.getOperation().getOperationId(), resourcePostfix, command.getExtension()),
            String.format("/%s/%s/%s%s", command.getResourceBasePath(), command.getOperation().getOperationId(), descriptorType.name().toLowerCase(), command.getExtension())
        );
    }

}
