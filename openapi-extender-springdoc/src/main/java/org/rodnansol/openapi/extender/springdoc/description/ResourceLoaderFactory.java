package org.rodnansol.openapi.extender.springdoc.description;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Resource loader factory.
 *
 * @author nandorholozsnyak
 * @since 0.3.1
 */
public class ResourceLoaderFactory {

    static final String SUMMARY_POSTFIX = "-summary";
    static final String DESCRIPTION_POSTFIX = "";

    private ResourceLoaderFactory() {
    }

    /**
     * Returns the following resource loaders with description postfix.
     *
     * <ul>
     *     <li>org.rodnansol.openapi.extender.springdoc.description.OperationIdBasedResourceLoader</li>
     *     <li>org.rodnansol.openapi.extender.springdoc.description.MethodNameBasedResourceLoader</li>
     *     <li>org.rodnansol.openapi.extender.springdoc.description.CustomExtensionBasedResourceLoader</li>
     * </ul>
     *
     * @return list of resource loaders.
     */
    public static List<ResourceLoader> getDescriptionResourceLoaders() {
        return Stream.of(
                new OperationIdBasedResourceLoader(DESCRIPTION_POSTFIX, DescriptorType.DESCRIPTION),
                new MethodNameBasedResourceLoader(DESCRIPTION_POSTFIX, DescriptorType.DESCRIPTION),
                new CustomExtensionBasedResourceLoader(OpenApiExtenderExtensionConstants.DESCRIPTION_KEY)
            )
            .collect(Collectors.toList());
    }

    /**
     * Returns the following resource loaders with description postfix with a custom path resolver:
     *
     * <ul>
     *     <li>org.rodnansol.openapi.extender.springdoc.description.OperationIdBasedResourceLoader</li>
     *     <li>org.rodnansol.openapi.extender.springdoc.description.MethodNameBasedResourceLoader</li>
     *     <li>org.rodnansol.openapi.extender.springdoc.description.CustomExtensionBasedResourceLoader</li>
     * </ul>
     *
     * @return list of resource loaders.
     */
    public static List<ResourceLoader> getDescriptionResourceLoaders(Function<LoadResourceCommand, Stream<String>> pathResolverFunction) {
        return Stream.of(
                new OperationIdBasedResourceLoader(pathResolverFunction),
                new MethodNameBasedResourceLoader(pathResolverFunction),
                new CustomExtensionBasedResourceLoader(OpenApiExtenderExtensionConstants.DESCRIPTION_KEY)
            )
            .collect(Collectors.toList());
    }


    /**
     * Returns the following resource loaders with summary postfix:
     *
     * <ul>
     *     <li>org.rodnansol.openapi.extender.springdoc.description.OperationIdBasedResourceLoader</li>
     *     <li>org.rodnansol.openapi.extender.springdoc.description.MethodNameBasedResourceLoader</li>
     *     <li>org.rodnansol.openapi.extender.springdoc.description.CustomExtensionBasedResourceLoader</li>
     * </ul>
     *
     * @return list of resource loaders.
     */
    public static List<ResourceLoader> getSummaryResourceLoaders() {
        return Stream.of(
                new OperationIdBasedResourceLoader(SUMMARY_POSTFIX, DescriptorType.SUMMARY),
                new MethodNameBasedResourceLoader(SUMMARY_POSTFIX, DescriptorType.SUMMARY),
                new CustomExtensionBasedResourceLoader(OpenApiExtenderExtensionConstants.SUMMARY_KEY)
            )
            .collect(Collectors.toList());
    }

    /**
     * Returns the following resource loaders with summary postfix with a custom path resolver:
     *
     * <ul>
     *     <li>org.rodnansol.openapi.extender.springdoc.description.OperationIdBasedResourceLoader</li>
     *     <li>org.rodnansol.openapi.extender.springdoc.description.MethodNameBasedResourceLoader</li>
     *     <li>org.rodnansol.openapi.extender.springdoc.description.CustomExtensionBasedResourceLoader</li>
     * </ul>
     *
     * @return list of resource loaders.
     */
    public static List<ResourceLoader> getSummaryResourceLoaders(Function<LoadResourceCommand, Stream<String>> pathResolverFunction) {
        return Stream.of(
                new OperationIdBasedResourceLoader(pathResolverFunction),
                new MethodNameBasedResourceLoader(pathResolverFunction),
                new CustomExtensionBasedResourceLoader(OpenApiExtenderExtensionConstants.SUMMARY_KEY)
            )
            .collect(Collectors.toList());
    }


}
