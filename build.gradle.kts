import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	val kotlinVersion = "1.4.21"
	kotlin("jvm") version kotlinVersion apply false
}

allprojects {
	repositories {
		mavenCentral()
	}
}

subprojects {
	group = "dev.ravindu"
	version = "1.0.1"

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			jvmTarget = "1.8"
		}
	}
}
