import org.gradle.api.file.DuplicatesStrategy

plugins {
	kotlin("jvm")
}

dependencies {
	implementation(project(":skgl-kotlin"))
	implementation("no.tornado:tornadofx:1.7.20")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
}

tasks.withType<Jar> {

	manifest {
		attributes["Main-Class"] = "dev.ravindu.skgl.ui.SkglUiApp"
	}

	// Include all dependencies in the executable archive.
	duplicatesStrategy = DuplicatesStrategy.INCLUDE
	from(configurations["runtimeClasspath"].map { if (it.isDirectory) it else zipTree(it) })
}
