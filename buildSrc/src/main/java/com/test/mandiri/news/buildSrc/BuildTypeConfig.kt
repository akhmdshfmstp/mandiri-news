package com.test.mandiri.news.buildSrc

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension

fun LibraryExtension.applyNonMinifyBuildTypes() {
    buildTypes {
        release {
            isMinifyEnabled = false
        }
        debug {
            isMinifyEnabled = false
        }
        create("sanity") {
            initWith(getByName("release"))
            matchingFallbacks += "debug"
        }
        create("demo") {
            initWith(getByName("debug"))
            matchingFallbacks += "debug"
        }
    }
}

fun ApplicationExtension.applyBuildTypes() {
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "app_name", "\"Mandiri News\"")
        }
        debug {
            isMinifyEnabled = false
            versionNameSuffix = getWorkingBranchAsVersionNameSuffix()
            applicationIdSuffix = ".debug"
            resValue("string", "app_name", "\"Mandiri News Debug\"")
        }
    }
}

fun getWorkingBranchAsVersionNameSuffix(): String {
    val workingBranch = System.getenv("BRANCH") ?: run {
        println("Cannot get env variable, probably manual build. Get from local.")
        try {
            val proc = ProcessBuilder("git", "rev-parse", "--abbrev-ref", "HEAD").start()
            proc.waitFor()
            if (proc.exitValue() == 0) {
                proc.inputStream.bufferedReader().readText().trim()
            } else {
                println("Git command failed, fallback to 'local'")
                "local"
            }
        } catch (e: Exception) {
            println("Failed to execute git: ${e.message}")
            "local"
        }
    }
    println("Working branch = $workingBranch")
    return when {
        workingBranch.startsWith("develop") || workingBranch == "main" -> ""
        workingBranch.startsWith("release/") -> "-RC"
        workingBranch.startsWith("hotfix/") -> "-HOTFIX"
        workingBranch.startsWith("fix/") -> "-FIX"
        else -> "-${workingBranch.removePrefix("feature/")}"
    }
}
