import org.gradle.jvm.toolchain.JavaLanguageVersion

object AppConstants {
    val javaVersion = JavaLanguageVersion.of(21)

    const val MAIN_CLASS = "net.dunice.newsapi.NewsApiApplication"
}