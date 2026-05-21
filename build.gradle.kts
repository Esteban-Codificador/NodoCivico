// Configuración a nivel de proyecto. Aquí se declaran los plugins comunes
// que utilizarán los módulos hijos. La aplicación real se configura en app/build.gradle.kts
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
}
