plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.google.guava:guava:29.0-jre")
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.7.0.202003110725-r")
}