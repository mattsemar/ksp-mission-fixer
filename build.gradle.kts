plugins {
    java
    id("org.beryx.jlink") version "2.23.8"
    id("org.javamodularity.moduleplugin") version "1.6.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}


group = "com.semarware"
version = "1.0-SNAPSHOT"



repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.12.3")
}


tasks.named<Test>("test") {
    useJUnitPlatform()
}

jlink {
    launcher {
        name = "kspMissionFixer"
        mainClass.set("com.semarware.ksp.Fixer")
    }
    jpackage {
        outputDir = "package"
        imageName = "KspMissionFixer"
        imageOptions = listOf("--win-console")
        skipInstaller = false
        installerName = "KspMissionFixer"
        installerType = "msi"
        installerOptions = listOf("--win-console", "--win-menu", "--win-shortcut")
    }

}
