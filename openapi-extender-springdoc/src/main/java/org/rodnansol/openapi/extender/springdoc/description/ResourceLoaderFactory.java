package org.rodnansol.openapi.extender.springdoc.description;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author nandorholozsnyak
 * @since 0.3.1
 */
public class ResourceLoaderFactory {


    static final String SUMMARY_POSTFIX = "-summary";
    static final String DESCRIPTION_POSTFIX = "";

    private ResourceLoaderFactory() {
    }

    public static List<ResourceLoader> getDescriptionResourceLoaders() {
        return Stream.of(
                new OperationIdBasedResourceLoader(DESCRIPTION_POSTFIX, DescriptorType.DESCRIPTION),
                new MethodNameBasedResourceLoader(DESCRIPTION_POSTFIX, DescriptorType.DESCRIPTION),
                new CustomExtensionBasedResourceLoader(OpenApiExtenderExtensionConstants.DESCRIPTION_KEY)
            )
            .collect(Collectors.toList());
    }

    public static List<ResourceLoader> getDescriptionResourceLoaders(Function<LoadResourceCommand, Stream<String>> pathResolverFunction) {
        return Stream.of(
                new OperationIdBasedResourceLoader(DESCRIPTION_POSTFIX, DescriptorType.DESCRIPTION, pathResolverFunction),
                new MethodNameBasedResourceLoader(DESCRIPTION_POSTFIX, DescriptorType.DESCRIPTION, pathResolverFunction),
                new CustomExtensionBasedResourceLoader(OpenApiExtenderExtensionConstants.DESCRIPTION_KEY)
            )
            .collect(Collectors.toList());
    }

    public static List<ResourceLoader> getSummaryResourceLoaders() {
        return Stream.of(
                new OperationIdBasedResourceLoader(SUMMARY_POSTFIX, DescriptorType.SUMMARY),
                new MethodNameBasedResourceLoader(SUMMARY_POSTFIX, DescriptorType.SUMMARY),
                new CustomExtensionBasedResourceLoader(OpenApiExtenderExtensionConstants.SUMMARY_KEY)
            )
            .collect(Collectors.toList());
    }

    public static List<ResourceLoader> getSummaryResourceLoaders(Function<LoadResourceCommand, Stream<String>> pathResolverFunction) {
        return Stream.of(
                new OperationIdBasedResourceLoader(SUMMARY_POSTFIX, DescriptorType.SUMMARY, pathResolverFunction),
                new MethodNameBasedResourceLoader(SUMMARY_POSTFIX, DescriptorType.SUMMARY, pathResolverFunction),
                new CustomExtensionBasedResourceLoader(OpenApiExtenderExtensionConstants.SUMMARY_KEY)
            )
            .collect(Collectors.toList());
    }


}
