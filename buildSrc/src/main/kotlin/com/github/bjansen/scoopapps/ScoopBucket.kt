package com.github.bjansen.scoopapps

data class ScoopBucket(val name: String, val url: String, val cloneUrl: String, val mainBranch: String = "main")
