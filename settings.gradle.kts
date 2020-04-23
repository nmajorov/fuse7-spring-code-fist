pluginManagement {
    val kotlinVersion : String by settings
    plugins {

        id("java")
        id ("org.jetbrains.kotlin.jvm") version "${kotlinVersion}"
        id ("org.springframework.boot") version ("2.2.6.RELEASE")
    }
}
