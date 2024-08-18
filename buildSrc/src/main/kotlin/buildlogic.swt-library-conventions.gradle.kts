import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    id("buildlogic.java-library-conventions")
}

var windowsBuild = properties.containsKey("windowsBuild")
var linuxBuild = properties.containsKey("linuxBuild")
var macosBuild = properties.containsKey("macosBuild")

if (!windowsBuild && !linuxBuild && !macosBuild) {
    windowsBuild = Os.isFamily(Os.FAMILY_WINDOWS)
    linuxBuild = Os.isFamily(Os.FAMILY_UNIX)
    macosBuild = Os.isFamily(Os.FAMILY_MAC)
}

dependencies {
    if (windowsBuild) {
        implementation("org.eclipse.platform:org.eclipse.swt.win32.win32.x86_64:3.126.0")
    } else if (macosBuild) {
        implementation("org.eclipse.platform:org.eclipse.swt.cocoa.macosx.aarch64:3.126.0")
    } else if (linuxBuild) {
        implementation("org.eclipse.platform:org.eclipse.swt.gtk.linux.x86_64:3.126.0")
    }
}

configurations.all {
    // Gradle does not resolve ${osgi.platform} like Maven
    exclude(group = "org.eclipse.platform", module = "org.eclipse.swt.\${osgi.platform}")
}