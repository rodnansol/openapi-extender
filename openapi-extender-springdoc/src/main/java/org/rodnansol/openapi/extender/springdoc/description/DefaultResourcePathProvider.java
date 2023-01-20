package org.rodnansol.openapi.extender.springdoc.description;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.method.HandlerMethod;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Class that returns the default provider functions.
 *
 * @author nandorholozsnyak
 * @since 0.3.1
 */
class DefaultResourcePathProvider {


    private DefaultResourcePathProvider() {
    }

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
     * @param resourcePostfix resource postfix
     * @param descriptorType  descriptor type
     * @return default function.
     */
    public static Function<LoadResourceCommand, Stream<String>> getMethodHandlerBasedFunction(String resourcePostfix, DescriptorType descriptorType) {
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
     *
     * @param resourcePostfix resource postfix
     * @param descriptorType  descriptor type
     * @return default function.
     */
    public static Function<LoadResourceCommand, Stream<String>> getOperationBasedFunction(String resourcePostfix, DescriptorType descriptorType) {
        //1. /<resourceBasePath>/<operationId><postFix><extension>
        //2. /<resourceBasePath>/<operationId>/<descriptorType><extension>
        return command -> Stream.of(
            String.format("/%s/%s%s%s", command.getResourceBasePath(), command.getOperation().getOperationId(), resourcePostfix, command.getExtension()),
            String.format("/%s/%s/%s%s", command.getResourceBasePath(), command.getOperation().getOperationId(), descriptorType.name().toLowerCase(), command.getExtension())
        );
    }

}
