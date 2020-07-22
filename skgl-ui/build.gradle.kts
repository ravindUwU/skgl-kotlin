plugins {
	kotlin("jvm")
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	implementation(project(":skgl-kotlin"))
	implementation("no.tornado:tornadofx:1.7.20")
}

tasks.withType<Jar> {

	manifest {
		attributes["Main-Class"] = "dev.ravindu.skgl.ui.SkglUiApp"
	}

	// Include all dependencies in the executable archive.
	from(configurations["runtimeClasspath"].map { if (it.isDirectory) it else zipTree(it) })
}
