package dev.erneto.utils

object Filer {

    fun fixName(name: String): String {
        return if (name.endsWith(".yml")) name else "$name.yml"
    }

    fun createFolders() {

    }
}