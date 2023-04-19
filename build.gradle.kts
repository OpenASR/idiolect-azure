import org.jetbrains.changelog.Changelog.OutputType.HTML

fun properties(key: String) = providers.gradleProperty(key)

plugins {
  kotlin("jvm") version "1.8.20"
  id("org.jetbrains.intellij") version "1.13.3"
  id("com.github.ben-manes.versions") version "0.46.0"
  id("org.jetbrains.changelog") version "2.0.0"
}

group = "org.openasr"
version = "1.0-SNAPSHOT"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  version = "2023.1" // The version of the IntelliJ Platform IDE that will be used to build the plugin
  pluginName = "idiolect-azure"
  updateSinceUntilBuild = false
  plugins = listOf("org.openasr.idiolect:1.4.8")
//  plugins = listOf("org.openasr.idiolect:1.4.9-SNAPSHOT@beta")
}

tasks {
  val jvmTarget = "17"
  compileKotlin { kotlinOptions.jvmTarget = jvmTarget }
  withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
  }
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
  }


  changelog {
    groups = listOf("Added", "Changed", "Removed", "Fixed")
  }

  patchPluginXml {
    version = "${project.version}"
    sinceBuild = "222"
//    untilBuild = "223.*")

    changeNotes = provider {
      changelog.renderItem(changelog.getAll().values.first(), HTML)
    }
  }

  compileTestKotlin {
    kotlinOptions.jvmTarget = jvmTarget
  }

  buildPlugin {
    dependsOn("test")
    archiveFileName.set("idiolect-azure.zip")
  }

  runIde {
    dependsOn("test")
    findProperty("luginDev")?.let { args = listOf(projectDir.absolutePath) }
  }

  if (System.getenv("GITHUB_REF_NAME") != null
    && !System.getenv("INTELLIJ_CERTIFICATE_CHAIN").isNullOrEmpty()
  ) {
    signPlugin {
      certificateChain.set(System.getenv("INTELLIJ_CERTIFICATE_CHAIN"))
      privateKey.set(System.getenv("INTELLIJ_PRIVATE_KEY"))
      password.set(System.getenv("INTELLIJ_PRIVATE_KEY_PASSWORD"))
      inputArchiveFile.set(File("./build/distributions/idiolect-azure.zip"))
      outputArchiveFile.set(File("./build/distributions/idiolect-azure-signed.zip"))
    }

    publishPlugin {
      distributionFile.set(File("./build/distributions/idiolect-azure-signed.zip"))
      if (System.getenv("GITHUB_REF_NAME") != "master") {
        // Users can configure a new custom plugin repository: https://plugins.jetbrains.com/plugins/eap/list
        // https://www.jetbrains.com/help/idea/managing-plugins.html#repos
        // alpha/beta/eap/canary
        channels.set(listOf(System.getenv("INTELLIJ_CHANNEL")))
        // ...could also add updatePlugins.xml to github site
        // https://plugins.jetbrains.com/docs/intellij/custom-plugin-repository.html#describing-your-plugins-in-updatepluginsxml-file
      }
      token.set(System.getenv("INTELLIJ_PUBLISH_TOKEN"))
    }
  }
}

repositories {
  mavenLocal()
  mavenCentral()
  maven("https://plugins.jetbrains.com/maven")
  maven("https://azureai.azureedge.net/maven")
}

dependencies {
//  implementation("org.openasr:idiolect-lib:1.0.0")
  implementation(
    group = "com.microsoft.cognitiveservices.speech",
    name = "client-sdk",
    version = "1.24.2",
    ext = "jar"
  )
}
