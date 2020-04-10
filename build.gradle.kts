import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.bjansen.scoopapps.ScoopAppsPlugin

// Buildscript dependencies and plugins
//----------------------------------------------------------------------------------------------------------------------

plugins {
    // Check latest version at https://plugins.gradle.org/plugin/com.eden.orchidPlugin
    id("com.eden.orchidPlugin") version "0.20.0"
    kotlin("jvm") version "1.3.60"
}

// Orchid setup
//----------------------------------------------------------------------------------------------------------------------
repositories {
    jcenter()
}

dependencies {
    val orchid_version = "0.20.0"
    compile("io.github.javaeden.orchid:OrchidCore:$orchid_version")
    orchidCompile("io.github.javaeden.orchid:OrchidCore:$orchid_version")
    orchidRuntime("io.github.javaeden.orchid:OrchidPages:$orchid_version")
    orchidRuntime("io.github.javaeden.orchid:OrchidSearch:$orchid_version")
    orchidRuntime("io.github.javaeden.orchid:OrchidWritersBlocks:$orchid_version")
    orchidRuntime("io.github.javaeden.orchid:OrchidSyntaxHighlighter:$orchid_version")
    orchidRuntime("io.github.javaeden.orchid:OrchidBsDoc:$orchid_version")
    orchidRuntime("io.github.javaeden.orchid:OrchidGithub:$orchid_version")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

fun envOrProperty(name: String, required: Boolean = false): String? {
    val result = project.findProperty(name) as? String ?: System.getenv(name)
    check(result != null || required.not()) { "Missing required environment property:\n  export $name=\"...\"" }
    return result
}

project.version = "0.1"

orchid {
    val isProd = envOrProperty("env") == "prod"
    version = "${project.version}"
    theme = "BsDoc"
    environment = if (isProd) "production" else "debug"
    baseUrl = when {
        isProd && envOrProperty("PULL_REQUEST") == "true" -> envOrProperty("DEPLOY_URL", required = true)
        isProd -> envOrProperty("URL", required = true)
        else -> "http://localhost:8080"
    }
    githubToken = project.property("githubToken") as String?
}
apply<ScoopAppsPlugin>()