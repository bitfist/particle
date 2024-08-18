plugins {
    java
    `java-library`
    jacoco
}

repositories {
    mavenLocal()
    mavenCentral()
}

java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.12.0")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        csv.required = true
    }
}