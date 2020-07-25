plugins {
    kotlin("js") version "1.4.0-rc"
    `maven-publish`
}

group = "net.kodein.kpres"
version = "1.0.0-kotlin-1.4.0-rc"

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
    implementation(kotlin("stdlib-js"))

    val reactVersion = "16.13.1"
    val reactRouterVersion = "5.1.2"
    val kotlinWrapperVersion = "pre.109-kotlin-1.4.0-rc"

    api("org.jetbrains:kotlin-react-dom:$reactVersion-$kotlinWrapperVersion")
    api("org.jetbrains:kotlin-react-router-dom:$reactRouterVersion-$kotlinWrapperVersion")
    api("org.jetbrains:kotlin-styled:1.0.0-$kotlinWrapperVersion")
}

publishing {
    publications {
        create<MavenPublication>("kpres") {
            from(components["kotlin"])
        }
    }
}
