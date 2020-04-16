package com.github.bjansen.scoopapps

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import org.gradle.api.GradleException
import org.gradle.api.Project
import java.io.File
import java.io.InputStreamReader

class ScoopAppsPlugin : org.gradle.api.Plugin<Project> {

    override fun apply(target: Project) {
        target.task("scanBuckets") {
            doLast {
                val bucketsConf = ScoopAppsPlugin::class.java.getResourceAsStream("/buckets.json")
                val parseReader:JsonArray = JsonParser.parseReader(InputStreamReader(bucketsConf)) as JsonArray
                val docSearchIndex = StringBuilder("[\n")

                parseReader.forEach {
                    val bucket = it as JsonObject
                    scan(target, bucket["name"].asString, bucket["url"].asString, File(bucket["cloneUrl"].asString), docSearchIndex)
                }

                docSearchIndex.delete(docSearchIndex.length - 2, docSearchIndex.length)
                docSearchIndex.append("\n]")
                project.buildDir.mkdirs()
                File(project.buildDir, "DocSearch-index.json").writeText(docSearchIndex.toString())
            }
        }
    }

    private fun scan(
        project: Project,
        bucketName: String,
        bucketUrl: String,
        cloneDirectory: File,
        docSearchIndex: StringBuilder
    ) {
        val pagesDir = File(project.rootDir, "src/orchid/resources/pages/$bucketName")

        preparePagesDirectory(pagesDir)

        val bucketIndexContent: StringBuilder = StringBuilder("""
            ---
            title: Index of bucket $bucketName - Scoop Apps
            ---

            <h1>Bucket "$bucketName"</h1>
            <table>
        """.trimIndent())

        val bucketDir = File(cloneDirectory, "bucket")

        bucketDir.listFiles()?.forEach {
            val appName = it.nameWithoutExtension
            val jsonContent = JsonParser.parseString(it.readText()) as JsonObject

            val appVersion = jsonContent["version"].asString
            val appDescription = jsonContent["description"].asString.replace("\"", "\\\"")
            val license = when (jsonContent["license"]) {
                is JsonPrimitive -> jsonContent["license"].asString
                else -> "Unknown"
            }


            File(pagesDir, "$appName.md").writeText("""
                ---
                template: app
                appName: $appName
                appDescription: "$appDescription"
                appManifestUrl: $bucketUrl/tree/master/bucket/$appName.json
                appUrl: "${jsonContent["homepage"].asString}"
                appVersion: "$appVersion"
                bucketName: $bucketName
                bucketUrl: $bucketUrl
                license: "$license"
                ---
            """.trimIndent())

            bucketIndexContent.append("""<tr><td><a href="$appName">$appName</a></td><td><code>$appVersion</code></td><td>$appDescription</td>""").append('\n')
            docSearchIndex.append("""{"bucket": "$bucketName", "name": "$appName", "description": "$appDescription"},""").append("\n")
        }

        bucketIndexContent.append("</table>")

        File(pagesDir, "index.md").writeText(bucketIndexContent.toString())
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
