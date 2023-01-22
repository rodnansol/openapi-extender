package org.rodnansol.openapi.extender.springdoc.description;

/**
 * Class containing constants that describes the libraries OpenAPI extension values.
 *
 * @author nandorholozsnyak
 * @since 0.3.1
 */
public class OpenApiExtenderExtensionConstants {

    /**
     * "Official" extension key for the library.
     */
    public static final String EXTENSION_KEY = "x-extender";

    /**
     * Key value for the summary injection.
     */
    public static final String SUMMARY_KEY = "summary";

    /**
     * Key value for the description injection.
     */
    public static final String DESCRIPTION_KEY = "description";


    private OpenApiExtenderExtensionConstants() {
    }

}
