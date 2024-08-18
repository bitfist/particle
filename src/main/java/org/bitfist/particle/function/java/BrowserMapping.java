package org.bitfist.particle.function.java;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for browser mappings. Marked methods will be exposed to a {@link org.eclipse.swt.browser.Browser} through
 * the window object.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BrowserMapping {

    /**
     * The prefix of the method. Applied on a class level, the prefix is used for all exposed methods.
     *
     * @return the prefix of the methods.
     */
    String prefix() default "";
}
