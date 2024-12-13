import org.gradle.jvm.toolchain.JavaLanguageVersion

object AppConstants {
    val javaVersion = JavaLanguageVersion.of(21)

    const val MAIN_CLASS = "net.dunice.newsapi.NewsApiApplication"

    const val EXCLUDE_LOGBACK_GROUP = "ch.qos.logback"

    const val EXCLUDE_LOGBACK_MODULE = "logback-classic"

    const val SPRING_CLOUD = "springCloudVersion"
}