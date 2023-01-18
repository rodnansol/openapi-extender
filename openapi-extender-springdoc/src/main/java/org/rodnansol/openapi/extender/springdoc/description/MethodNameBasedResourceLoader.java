package org.rodnansol.openapi.extender.springdoc.description;

import org.springframework.util.Assert;

import java.util.function.Function;
import java.util.stream.Stream;

class MethodNameBasedResourceLoader extends AbstractResourceLoader implements ResourceLoader {

    private final String resourcePostfix;
    private final DescriptorType descriptorType;
    private final Function<LoadResourceCommand, Stream<String>> pathResolverFunction;

    MethodNameBasedResourceLoader(String resourcePostfix,
                                  DescriptorType descriptorType) {
        this.resourcePostfix = resourcePostfix;
        this.descriptorType = descriptorType;
        this.pathResolverFunction = getDefaultPathResolverFunction();
    }

    MethodNameBasedResourceLoader(String resourcePostfix,
                                  DescriptorType descriptorType,
                                  Function<LoadResourceCommand, Stream<String>> pathResolverFunction) {
        this.resourcePostfix = resourcePostfix;
        this.descriptorType = descriptorType;
        this.pathResolverFunction = pathResolverFunction;
    }

    @Override
    public byte[] loadResource(LoadResourceCommand command) {
        Assert.notNull(command, "command is NULL");
        Assert.notNull(command.getHandlerMethod(), "handlerMethod is NULL");

        return pathResolverFunction.apply(command)
            .map(this::loadResourceByPath)
            .filter(bytes -> bytes != null && bytes.length > 0)
            .findFirst()
            .orElse(new byte[0]);
    }

    private Function<LoadResourceCommand, Stream<String>> getDefaultPathResolverFunction() {
        //1. /<resourceBasePath>/<fullyQualifiedMethodName><postFix><extension>
        //2. /<resourceBasePath>/<className>/<method><descriptorType><extension>
        return command -> {
            String className = command.getHandlerMethod().getBeanType().getName();
            String methodName = command.getHandlerMethod().getMethod().getName();
            String fullyQualifiedMethodName = className + "#" + methodName;
            return Stream.of(
                String.format("/%s/%s%s%s", command.getResourceBasePath(), fullyQualifiedMethodName, resourcePostfix, command.getExtension()),
                String.format("/%s/%s/%s-%s%s", command.getResourceBasePath(), className, methodName, descriptorType.name().toLowerCase(), command.getExtension())
            );
        };
    }
}
