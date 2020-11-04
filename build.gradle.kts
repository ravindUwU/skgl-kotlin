import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	val kotlinVersion = "1.3.72"
	kotlin("jvm") version kotlinVersion apply false
}

allprojects {
	repositories {
		mavenCentral()
	}
}

subprojects {
	group = "dev.ravindu"
	version = "1.0.0"

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			jvmTarget = "1.8"
		}
	}
}
