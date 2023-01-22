package org.rodnansol.openapi.extender.springdoc.description;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.util.Assert;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Resource loader implementation that tries to resolve the resources based on the {@link io.swagger.v3.oas.annotations.Operation} annotation's {@link Operation#operationId()} value.
 * <p>
 * By default, the resources will be seeked in the following paths:
 * <ol>
 *   <li>/&lt;resourceBasePath&gt;/&lt;operationId&gt;&lt;postFix&gt;&lt;extension&gt; </li>
 *   <li> /&lt;resourceBasePath&gt;/&lt;operationId&gt;/&lt;descriptorType&gt;&lt;extension&gt; </li>
 * </ol>
 * For example:
 * <ul>
 *   <li>/operations/getUser-summary.md </li>
 *   <li> /operations/postUser/description.md</li>
 * </ul>
 * <p>
 * The different path can be defined within the constructor.
 *
 * @author nandorholozsnyak
 * @since 0.3.1
 */
class OperationIdBasedResourceLoader extends AbstractResourceLoader implements ResourceLoader {

    private final Function<LoadResourceCommand, Stream<String>> pathResolverFunction;

    OperationIdBasedResourceLoader(String resourcePostfix,
                                   DescriptorType descriptorType) {
        this.pathResolverFunction = DefaultResourcePathProvider.getOperationBasedFunction(resourcePostfix, descriptorType);
    }

    OperationIdBasedResourceLoader(Function<LoadResourceCommand, Stream<String>> pathResolverFunction) {
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

}
