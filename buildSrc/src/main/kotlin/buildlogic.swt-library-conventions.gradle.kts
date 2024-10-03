import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    id("buildlogic.java-library-conventions")
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

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
        implementation(libs.swtWindows)
    } else if (macosBuild) {
        implementation(libs.swtMacos)
    } else if (linuxBuild) {
        implementation(libs.swtLinux)
    }
}

configurations.all {
    // Gradle does not resolve ${osgi.platform} like Maven
    exclude(group = "org.eclipse.platform", module = "org.eclipse.swt.\${osgi.platform}")
}