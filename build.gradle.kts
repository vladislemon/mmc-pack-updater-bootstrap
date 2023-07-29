plugins {
    id("java")
}

group = "net.vladislemon"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "net.vladislemon.mmcpu.bootstrap.Main"
    }
}

tasks.test {
    useJUnitPlatform()
}