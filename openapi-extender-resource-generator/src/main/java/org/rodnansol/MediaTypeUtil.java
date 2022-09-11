package org.rodnansol;

import java.util.HashMap;
import java.util.Map;

public class MediaTypeUtil {

    static final Map<String, String> MEDIA_TYPES;

    static {
        MEDIA_TYPES = new HashMap<>();
        MEDIA_TYPES.put("application/json", ".json");
        MEDIA_TYPES.put("application/xml", ".xml");
    }

    private MediaTypeUtil() {
    }

}