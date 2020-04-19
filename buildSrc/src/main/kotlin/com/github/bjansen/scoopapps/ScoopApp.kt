package com.github.bjansen.scoopapps

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

class ScoopApp(
    val bucket: ScoopBucket,
    val appName: String,
    jsonContent: JsonObject
) {

    val appVersion = jsonContent["version"].asString
    val appDescription = when(jsonContent["description"]) {
        is JsonPrimitive -> jsonContent["description"].asString
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
        else -> ""
    }
    val license = when (jsonContent["license"]) {
        is JsonPrimitive -> jsonContent["license"].asString
        else -> "Unknown"
    }
    val homepage = jsonContent["homepage"].asString
}