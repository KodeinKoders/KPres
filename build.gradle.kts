plugins {
    kotlin("js") version "1.4.10"
    `maven-publish`
}

group = "net.kodein.kpres"
version = "1.1.0"

repositories {
    jcenter()
    maven( url = "https://kotlin.bintray.com/kotlin-js-wrappers")
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

kotlin {
    js {
        browser {}
        useCommonJs()
    }
}

dependencies {
    val reactVersion = "17.0.0"
    val reactRouterVersion = "5.2.0"
    val styledVersion = "5.2.0"
    val kotlinWrapperVersion = "pre.129-kotlin-1.4.10"

    api("org.jetbrains:kotlin-react-dom:$reactVersion-$kotlinWrapperVersion")
    api("org.jetbrains:kotlin-react-router-dom:$reactRouterVersion-$kotlinWrapperVersion")
    api("org.jetbrains:kotlin-styled:$styledVersion-$kotlinWrapperVersion")

    api(npm("highlight.js", "^10.3.2"))
    api(npm("react-markdown", "^5.0.2"))
}

publishing {
    publications {
        create<MavenPublication>("kpres") {
            from(components["kotlin"])
        }
    }
}
