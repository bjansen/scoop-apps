package com.github.bjansen.scoopapps

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.gradle.api.GradleException
import org.gradle.api.Project
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import org.eclipse.jgit.api.Git
import org.slf4j.LoggerFactory
import java.io.File
import java.io.InputStreamReader

class ScoopAppsPlugin : org.gradle.api.Plugin<Project> {

    val logger = LoggerFactory.getLogger(ScoopAppsPlugin::class.java)

    override fun apply(target: Project) {
        target.task("scanBuckets") {
            doLast {
                val knownBuckets = loadKnownBuckets(target)

                updateBuckets(knownBuckets)

                val apps: Multimap<ScoopBucket, ScoopApp> = loadApps(knownBuckets)

                generatePages(project, apps)
                generateDocSearchIndex(project, apps)
            }
        }
    }

    private fun updateBuckets(knownBuckets: List<ScoopBucket>) {
        knownBuckets.forEach { bucket ->
            val repository = File(bucket.cloneUrl)

            if (repository.isDirectory) {
                logger.warn("Updating bucket $bucket")
                Git.open(repository)
                    .pull()
                    .call()
            } else {
                logger.warn("Cloning bucket $bucket")
                Git.cloneRepository()
                    .setURI(bucket.url)
                    .setDirectory(repository)
                    .call()
            }
        }
    }

    private fun generatePages(project: Project, apps: Multimap<ScoopBucket, ScoopApp>) {
        // Initialize directory
        apps.keySet()
            .forEach {
                val pagesDir = File(project.rootDir, "src/orchid/resources/pages/${it.name}")
                preparePagesDirectory(pagesDir)
            }

        // Write apps pages
        apps.entries().forEach {
            val bucket = it.key
            val app = it.value
            val pagesDir = File(project.rootDir, "src/orchid/resources/pages/${bucket.name}")

            File(pagesDir, "${app.appName}.md").writeText("""
                ---
                template: app
                appName: ${app.appName}
                appDescription: "${app.appDescription}"
                appManifestUrl: ${app.bucket.url}/tree/master/bucket/${app.appName}.json
                appUrl: "${app.homepage}"
                appVersion: "${app.appVersion}"
                bucketName: ${app.bucket.name}
                bucketUrl: ${app.bucket.url}
                license: "${app.license}"
                ---
            """.trimIndent())
        }

        // Write bucket indexes
        apps.keySet().forEach {
                val bucketName = it.name
                val bucketIndexContent: StringBuilder = StringBuilder("""
                    ---
                    title: Index of bucket $bucketName - Scoop Apps
                    ---
        
                    <h1>Bucket "$bucketName"</h1>
                    <table>
                    
                """.trimIndent())

                apps.get(it)
                    .forEach { app ->
                        bucketIndexContent.append("""<tr><td><a href="${app.appName}">${app.appName}</a></td><td><code>${app.appVersion}</code></td><td>${app.appDescription}</td>""")
                            .append('\n')
                    }
                bucketIndexContent.append("</table>")

                val pagesDir = File(project.rootDir, "src/orchid/resources/pages/$bucketName")
                File(pagesDir, "index.md").writeText(bucketIndexContent.toString())
            }
    }

    private fun generateDocSearchIndex(project: Project, apps: Multimap<ScoopBucket, ScoopApp>) {
        val docSearchIndex = StringBuilder("[\n")

        apps.values().forEach {
            docSearchIndex.append("""{"bucket": "${it.bucket.name}", "name": "${it.appName}", "description": "${it.appDescription}"},""").append("\n")
        }

        docSearchIndex.delete(docSearchIndex.length - 2, docSearchIndex.length)
        docSearchIndex.append("\n]")
        project.buildDir.mkdirs()
        File(project.buildDir, "DocSearch-index.json").writeText(docSearchIndex.toString())
    }

    private fun loadApps(knownBuckets: List<ScoopBucket>): Multimap<ScoopBucket, ScoopApp> {
        val apps = ArrayListMultimap.create<ScoopBucket, ScoopApp>()

        knownBuckets.forEach {
            val bucket = it
            val bucketDir = File(bucket.cloneUrl, "bucket")

            bucketDir.listFiles()?.forEach { file ->
                val appName = file.nameWithoutExtension
                val jsonContent = JsonParser.parseString(file.readText()) as JsonObject
                apps.put(bucket, ScoopApp(bucket, appName, jsonContent))
            }
        }
        return apps
    }

    private fun loadKnownBuckets(target: Project): List<ScoopBucket> {
        val bucketsConf = ScoopAppsPlugin::class.java.getResourceAsStream("/buckets.json")
        return (JsonParser.parseReader(InputStreamReader(bucketsConf)) as JsonArray)
            .map { el ->
                val bucket = el as JsonObject
                val bucketDirectory = "${target.buildDir}/buckets/${bucket["name"].asString}"
                ScoopBucket(bucket["name"].asString, bucket["url"].asString, bucketDirectory)
            }
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
