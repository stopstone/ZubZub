import org.gradle.api.artifacts.VersionCatalog

fun VersionCatalog.getLibrary(library: String) = findLibrary(library).get()