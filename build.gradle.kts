plugins {
    java
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