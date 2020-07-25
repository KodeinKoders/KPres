plugins {
    kotlin("js") version "1.3.72"
    `maven-publish`
}

group = "net.kodein.kpres"
version = "1.0.0"

repositories {
    jcenter()
    maven( url = "https://kotlin.bintray.com/kotlin-js-wrappers")
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

kotlin {
    target {
        browser {}
        useCommonJs()
    }
}

dependencies {
    implementation(kotlin("stdlib-js"))

    val reactVersion = "16.13.1"
    val reactRouterVersion = "5.1.2"
    val kotlinWrapperVersion = "pre.110-kotlin-1.3.72"

    api("org.jetbrains:kotlin-react-dom:$reactVersion-$kotlinWrapperVersion")
    api("org.jetbrains:kotlin-react-router-dom:$reactRouterVersion-$kotlinWrapperVersion")
    api("org.jetbrains:kotlin-styled:1.0.0-$kotlinWrapperVersion")

    api(npm("highlight.js", "^10.1.2"))
    api(npm("react-markdown", "^4.3.1"))
}

publishing {
    publications {
        create<MavenPublication>("kpres") {
            from(components["kotlin"])
        }
    }
}
