package org.rodnansol.openapi.extender.springdoc.description;

import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;

import java.util.function.Function;
import java.util.stream.Stream;


/**
 * Resource loader implementation that tries to resolve the resources based on the {@link HandlerMethod} Spring based MVC method's name.
 * <p>
 * By default, the resources will be seeked in the following paths:
 * <ol>
 *   <li>/&lt;resourceBasePath&gt;/&lt;fullyQualifiedMethodName&gt;&lt;postFix&gt;&lt;extension&gt; </li>
 *   <li> /&lt;resourceBasePath&gt;/&lt;className&gt;/&lt;method&gt;-&lt;descriptorType&gt;&lt;extension&gt; </li>
 * </ol>
 * <p>
 * The different path can be defined within the constructor.
 * For example:
 * <ul>
 *   <li>/operations/org.rodnansol.user.UserController#getUser-summary.md</li>
 *   <li>/operations/org.rodnansol.user.UserController/postUser/description.md</li>
 * </ul>
 *
 * @author nandorholozsnyak
 * @since 0.3.1
 */
class MethodNameBasedResourceLoader extends AbstractResourceLoader implements ResourceLoader {

    private final Function<LoadResourceCommand, Stream<String>> pathResolverFunction;

    MethodNameBasedResourceLoader(String resourcePostfix,
                                  DescriptorType descriptorType) {
        this.pathResolverFunction = DefaultResourcePathProvider.getMethodHandlerBasedFunction(resourcePostfix, descriptorType);
    }

    MethodNameBasedResourceLoader(Function<LoadResourceCommand, Stream<String>> pathResolverFunction) {
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

}
