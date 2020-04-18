package com.github.bjansen.scoopapps

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

class ScoopApp(
    val bucket: ScoopBucket,
    val appName: String,
    jsonContent: JsonObject
) {

    val appVersion = jsonContent["version"].asString
    val appDescription = jsonContent["description"].asString.replace("\"", "\\\"")
    val license = when (jsonContent["license"]) {
        is JsonPrimitive -> jsonContent["license"].asString
        else -> "Unknown"
    }
    val homepage = jsonContent["homepage"].asString
}