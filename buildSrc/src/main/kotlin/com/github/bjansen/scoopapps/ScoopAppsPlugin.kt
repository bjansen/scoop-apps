package com.github.bjansen.scoopapps

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.gradle.api.GradleException
import org.gradle.api.Project
import java.io.File
import java.io.InputStreamReader

class ScoopAppsPlugin : org.gradle.api.Plugin<Project> {

    override fun apply(target: Project) {
        target.task("scanBuckets") {
            doLast {
                val knownBuckets: JsonArray = loadKnownBuckets()
                val apps: List<ScoopApp> = loadApps(knownBuckets)

                generatePages(project, apps)
                generateDocSearchIndex(project, apps)
            }
        }
    }

    private fun generatePages(project: Project, apps: List<ScoopApp>) {
        // Initialize directory
        apps.map(ScoopApp::bucketName)
            .distinct()
            .forEach {
                val pagesDir = File(project.rootDir, "src/orchid/resources/pages/$it")
                preparePagesDirectory(pagesDir)
            }

        // Write apps pages
        apps.forEach {
            val pagesDir = File(project.rootDir, "src/orchid/resources/pages/${it.bucketName}")

            File(pagesDir, "${it.appName}.md").writeText("""
                ---
                template: app
                appName: ${it.appName}
                appDescription: "${it.appDescription}"
                appManifestUrl: ${it.bucketUrl}/tree/master/bucket/${it.appName}.json
                appUrl: "${it.homepage}"
                appVersion: "${it.appVersion}"
                bucketName: ${it.bucketName}
                bucketUrl: ${it.bucketUrl}
                license: "${it.license}"
                ---
            """.trimIndent())
        }

        // Write bucket indexes
        apps.groupBy(ScoopApp::bucketName)
            .forEach {
                val bucketName = it.key
                val bucketIndexContent: StringBuilder = StringBuilder("""
                    ---
                    title: Index of bucket $bucketName - Scoop Apps
                    ---
        
                    <h1>Bucket "$bucketName"</h1>
                    <table>
                    
                """.trimIndent())

                apps.filter { app -> app.bucketName == bucketName }
                    .forEach { app ->
                        bucketIndexContent.append("""<tr><td><a href="${app.appName}">${app.appName}</a></td><td><code>${app.appVersion}</code></td><td>${app.appDescription}</td>""")
                            .append('\n')
                    }
                bucketIndexContent.append("</table>")

                val pagesDir = File(project.rootDir, "src/orchid/resources/pages/$bucketName")
                File(pagesDir, "index.md").writeText(bucketIndexContent.toString())
            }
    }

    private fun generateDocSearchIndex(project: Project, apps: List<ScoopApp>) {
        val docSearchIndex = StringBuilder("[\n")

        apps.forEach {
            docSearchIndex.append("""{"bucket": "${it.bucketName}", "name": "${it.appName}", "description": "${it.appDescription}"},""").append("\n")
        }

        docSearchIndex.delete(docSearchIndex.length - 2, docSearchIndex.length)
        docSearchIndex.append("\n]")
        project.buildDir.mkdirs()
        File(project.buildDir, "DocSearch-index.json").writeText(docSearchIndex.toString())
    }

    private fun loadApps(knownBuckets: JsonArray): List<ScoopApp> {
        val apps = ArrayList<ScoopApp>()

        knownBuckets.forEach {
            val bucket = it as JsonObject
            val bucketDir = File(bucket["cloneUrl"].asString, "bucket")

            bucketDir.listFiles()?.forEach { file ->
                val appName = file.nameWithoutExtension
                val jsonContent = JsonParser.parseString(file.readText()) as JsonObject
                apps.add(ScoopApp(bucket["name"].asString, bucket["url"].asString, appName, jsonContent))
            }
        }
        return apps
    }

    private fun loadKnownBuckets(): JsonArray {
        val bucketsConf = ScoopAppsPlugin::class.java.getResourceAsStream("/buckets.json")
        return JsonParser.parseReader(InputStreamReader(bucketsConf)) as JsonArray
    }

    private fun preparePagesDirectory(pagesDir: File) {
        if (pagesDir.isDirectory) {
            pagesDir.deleteRecursively()
        }

        if (!pagesDir.exists() && !pagesDir.mkdirs()) {
            throw GradleException("Can't create target pages directory '$pagesDir'")
        }
    }
}
