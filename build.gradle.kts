plugins {
    kotlin("js") version "1.5.31"
    `maven-publish`
}

group = "net.kodein.kpres"
version = "2.0.0"

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
        useCommonJs()
        binaries.executable()
    }
}

dependencies {
    val reactVersion = "17.0.2"
    val reactRouterVersion = "5.2.0"
    val styledVersion = "5.3.1"
    val kotlinWrapperVersion = "pre.248-kotlin-1.5.31"

    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:$reactVersion-$kotlinWrapperVersion")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:$reactVersion-$kotlinWrapperVersion")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:$reactRouterVersion-$kotlinWrapperVersion")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:$styledVersion-$kotlinWrapperVersion")

    api(npm("highlight.js", "^11.2.0"))
    api(npm("react-markdown", "7.0.0"))
}

publishing {
    publications {
        create<MavenPublication>("kpres") {
            from(components["kotlin"])
        }
    }
}
