plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.3.0.202209071007-r")

    implementation("com.algolia:algoliasearch-client-kotlin-jvm:2.1.1")
    implementation("io.ktor:ktor-client-apache:2.1.3")
}