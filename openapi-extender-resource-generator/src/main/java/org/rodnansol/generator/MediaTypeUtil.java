package org.rodnansol.generator;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class that stores media type related helper functions.
 */
public final class MediaTypeUtil {

    static final Map<String, String> MEDIA_TYPES;

    static {
        MEDIA_TYPES = new HashMap<>();
        MEDIA_TYPES.put("application/json", ".json");
        MEDIA_TYPES.put("application/xml", ".xml");
        MEDIA_TYPES.put("application/yaml", ".yaml");
    }

    private MediaTypeUtil() {
    }

}
