package org.rodnansol.openapi.extender.swagger.core.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Context class that stores all processed example references.
 */
public class ExampleReferenceContext {

    private static final ExampleReferenceContext INSTANCE = new ExampleReferenceContext();

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleReferenceContext.class);

    private final Map<ExampleReferenceKey, List<ExampleReference>> references = new HashMap<>();

    private ExampleReferenceContext() {
    }

    /**
     * Returns the singleton instance.
     *
     * @return singleton instance.
     */
    public static ExampleReferenceContext getInstance() {
        return INSTANCE;
    }

    /**
     * Adds a new entry into the context.
     *
     * @param referenceKey     example reference key object.
     * @param exampleReference example reference itself.
     * @return added example reference instance.
     */
    public void addReference(ExampleReferenceKey referenceKey, ExampleReference exampleReference) {
        LinkedList<ExampleReference> holder;
        if (!references.containsKey(referenceKey)) {
            holder = new LinkedList<>();
            holder.add(exampleReference);
            references.put(referenceKey, holder);
        } else {
            references.get(referenceKey).add(exampleReference);
        }
    }

    /**
     * Returns a list of references by key.
     *
     * @param referenceKey reference key instance.
     * @return list of associated references.
     */
    public List<ExampleReference> getReference(ExampleReferenceKey referenceKey) {
        return references.get(referenceKey);
    }

    /**
     * Returns all reference entries.
     *
     * @return all reference entries.
     */
    public Map<ExampleReferenceKey, List<ExampleReference>> getReferences() {
        return Collections.unmodifiableMap(references);
    }

    /**
     * Clears the context to free up space.
     */
    public void clearContext() {
        LOGGER.debug("Clearing ExampleReferenceContext to free up space.");
        references.clear();
    }
}
