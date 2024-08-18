![Coverage](.github/badges/jacoco.svg)
![Branches](.github/badges/branches.svg)

# Particle

Particle is a Java alternative to [Electron](https://www.electronjs.org/). It is based on SWTs
[Browser](https://download.eclipse.org/rt/rap/doc/2.3/guide/reference/api/org/eclipse/swt/browser/Browser.html) and
uses a native browser engine (WebKit on Linux & macOS, Edge on Windows).

> [!WARNING]
>
> This project is in active development, and caution is advised when considering it for production uses. You _can_ use
> it, but you should expect APIs to change often, things to move around and/or break, and all that jazz. Binary
> compatibility is not guaranteed across releases, and APIs are still in flux and subject to change.

## Motivation

I really like HTML & CSS for UI, but I very much dislike Electron. This led me down a rabbit whole, where I accidentally 
stumbled upon the SWT Browser widget. This widget uses the system browser engine and allows bidirectional calls
(`BrowserFunction`s to call Java from JavaScript, and `browser.execute` to execute JavaScript from Java). This project
aims to make usage of this widget easier.

## Requirements

* Java 17
* The correct SWT library in your classpath (OS and CPU architecture)

## Restrictions
* Right now testing is a huge question mark as SWT makes no effort to be testable outside the Eclipse world, e.g. there
is SWTBot for UI testing, but no Maven artifacts are provided. 

## Features
* Easier SWT setup with Particle Window
* Expose Java functions to the browser with Particle Function Java
* Execute JavaScript code in the browser using Particle Function JavaScript

### Particle Window

This is a convenience project for easier setup of an SWT display, shell and browser by providing the
[ParticleWindow](src/main/java/org/bitfist/particle/ParticleWindow.java). The browser widget takes the full size
of the shell.

#### Restrictions

Although it is possible to provide HTML to the browser through `browser.setText`, Particle Window expects a URL (either
a web URL or a file URL), since `browser.setText` does not properly work with Microsoft Edge. In case of a file URL,
make sure that all JavaScript and CSS is inlined, otherwise you will run into issues with CSP (Content Security Policy).

#### Recommendations
* Build a single HTML file with inlined JavaScript, CSS and images
* Copy the HTML file into the user home folder, eg `~/.my-application/index.html`
* Provide a file URL to the index file

--------------------------------------------------------------

### Particle Function Java

This project provides the annotation [BrowserMapping](src/main/java/org/bitfist/particle/function/java/BrowserMapping.java),
which can be used to mark classes and methods to be exposed through the `window` object inside the
[Browser](https://download.eclipse.org/rt/rap/doc/2.3/guide/reference/api/org/eclipse/swt/browser/Browser.html) widget.
The [BrowserMappingProcessor](src/main/java/org/bitfist/particle/function/java/BrowserMappingProcessor.java) processes
objects containing this annotation to the provided browser.

#### Usage

```java
@BrowserMapping(prefix = "incrementer")
public class Incrementer {

    private int number = 0;

    @BrowserMapping
    public int increment() {
        number++;
        return number;
    }
}
```

This code will expose the increment function through `window.incrementer_increment()` inside the browser.

#### Object support & JavaScript

By default, SWTs own [BrowserFunction](https://download.eclipse.org/rt/rap/doc/2.3/guide/reference/api/org/eclipse/swt/browser/BrowserFunction.html)
only supports primitive types. This project provides support for objects by using a Jackson `ObjectMapper` to turn those
objects into a String and parsing String into objects. This means that the JavaScript side has to use `JSON.parse` and
`JSON.stringify` for communication.

--------------------------------------------------------------

### Particle Function JavaScript

This project provides support for executing JavaScript code inside the browser. The code can be provided through
interfaces annotated with `@JavaScriptFile`, whose methods are annotated with `@JavaScriptCode("js-code")`.

#### Usage

```java
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
```

#### Restrictions
* JavaScript cannot return values; behind the scenes, `browser.execute` is used to execute the code, which only returns
  `true` on successful execution, `false` otherwise 