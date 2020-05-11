/*
 * This file was generated by the Gradle "init" task.
 */


import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    val kotlinVersion: String by project

    repositories {
        mavenCentral()
    }

    dependencies {
        kotlin("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
    }
}




plugins {
    // reading versions and config from settings.gradle.kts
    id("java")
    id("org.springframework.boot")
    id("org.jetbrains.kotlin.jvm")
}



repositories {
    mavenLocal()
    mavenCentral()

    maven {
        setUrl("http://maven.repository.redhat.com/techpreview/all/")
        setUrl("https://repo.fusesource.com/nexus/content/groups/public/")
        setUrl("https://maven.repository.redhat.com/earlyaccess/all")
    }
}




dependencies {

    //get from properties
    val kotlinVersion: String by project




    api("org.apache.cxf:cxf-spring-boot-starter-jaxrs:3.1.11") {
        exclude(

                group = "org.springframework.boot", module = "spring-boot-starter-tomcat"
        )
        exclude(group = "org.apache.cxf", module = "cxf-rt-frontend-jaxrs")
        exclude(group = "org.apache.cxf", module = "cxf-rt-transports-http")
        exclude(group = "org.apache.cxf", module = "cxf-rt-rs-client")
    }

    api("org.springframework.boot:spring-boot-starter-undertow:1.5.4.RELEASE")
    api("org.springframework.boot:spring-boot-starter-actuator:1.5.4.RELEASE")
    api("org.webjars:swagger-ui:3.24.3")
    api("org.apache.cxf:cxf-rt-rs-service-description-swagger:3.1.11") {
        exclude(group = "org.apache.cxf", module = "cxf-rt-frontend-jaxrs")
    }


    api("io.swagger:swagger-jaxrs:1.5.10") {
        exclude(group = "javax.ws.rs", module = "jsr311-api")
    }

    api(group = "com.fasterxml.jackson.jaxrs",
            name = "jackson-jaxrs-json-provider",
            version = "2.8.8"
    )

    api("org.apache.camel:camel-spring-boot-starter:2.19.1")
    api("org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}")
    api("org.hsqldb:hsqldb:2.3.5")
    api("org.apache.camel:camel-sql:2.19.1")
    api("org.apache.camel:camel-jackson:2.19.1")
    api("com.mchange:c3p0:0.9.5.3")


    api("org.apache.cxf:cxf-rt-frontend-jaxrs:3.3.3")

   implementation("javax.xml.ws:jaxws-api:2.3.1")

  //  api("org.apache.cxf:cxf-rt-frontend-jaxrs:3.3.3") {
  //      exclude(group = "org.apache.cxf", module = "cxf-core")
  //      exclude(group = "org.apache.cxf", module = "cxf-rt-transports-http")
  //  }

    api("org.apache.cxf:cxf-rt-rs-client:3.3.3") {
        exclude(group = "org.apache.cxf", module = "cxf-core")
        exclude(group = "org.apache.cxf", module = "cxf-rt-transports-http")
    }

    api("org.apache.cxf:cxf-core:3.3.3")
    api("org.apache.cxf:cxf-rt-transports-http:3.3.3")
    api("javax.ws.rs:javax.ws.rs-api:2.1.1")

    //hawtion console
    api("io.hawt:hawtio-springboot-1:2.0.0.fuse-740026-redhat-00001")



    testImplementation("junit:junit:4.12")
    testImplementation("org.jboss.arquillian.junit:arquillian-junit-container:1.1.12.Final")
    testImplementation("org.arquillian.cube:arquillian-cube-openshift:1.9.0")
    testImplementation("io.fabric8:kubernetes-assertions:2.3.7")
    testImplementation("org.springframework.boot:spring-boot-starter-test:1.5.4.RELEASE")

}

group = "io.fabric8.quickstarts.cxf.jaxrs"
version = "1.0-SNAPSHOT"
description = "Fabric8 :: Quickstarts :: Spring-Boot :: CXF JAXRS"



tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget="1.8"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
   
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8" 
}


tasks {
    test {
        exclude("**/**/KubernetesIntegrationKT.class")
    }
}
