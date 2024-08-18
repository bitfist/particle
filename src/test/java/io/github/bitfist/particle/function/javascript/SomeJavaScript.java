package io.github.bitfist.particle.function.javascript;

@JavaScriptFile
public interface SomeJavaScript {

    @JavaScriptCode(
            // language=JavaScript
            """
            document.body.style.backgroundColor = "red";
            """
    )
    void makeBodyRed();

}
