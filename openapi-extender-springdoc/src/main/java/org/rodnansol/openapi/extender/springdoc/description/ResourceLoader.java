package org.rodnansol.openapi.extender.springdoc.description;

/**
 * Resource loader interface.
 *
 * @author nandorholozsnyak
 * @since 0.3.1
 */
interface ResourceLoader {

    /**
     * Tries to locate a resource and load it based on the incoming command.
     *
     * @param loadResourceCommand command containing information about the resource to be loaded.
     * @return located and loaded resource in a byte array, if the resource does not exist or can not be located it must be an <b>empty array</b>.
     */
    byte[] loadResource(LoadResourceCommand loadResourceCommand);

}
