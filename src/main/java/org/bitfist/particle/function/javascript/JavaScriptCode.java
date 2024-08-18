package org.bitfist.particle.function.javascript;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate an interface method with this annotation and provide some JavaScript code that will be executed inside the
 * browser. $0$, $1$ and so on will be replaced with the arguments provided to the function.
 * <h1>Restrictions</h1>
 * <ol>
 *     <li>Only primitive arguments are supported for now. You can use Jackson to stringify objects.</li>
 *     <li>No return values are supported. If you want to send data to Java, call a <b>@BrowserMapping</b> function from
 *     your JavaScript code using the <b>window</b> object</li>
 * </ol>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JavaScriptCode {

    /**
     * This value should be valid JavaScript code.
     */
    String value();

}
