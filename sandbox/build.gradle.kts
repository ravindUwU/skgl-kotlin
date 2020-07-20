plugins {
	kotlin("jvm")
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	implementation(project(":skgl-kotlin"))
}

tasks.withType<Jar> {

	manifest {
		attributes["Main-Class"] = "com.example.program.Program"
	}

	// Include all dependencies in the executable archive.
	from(configurations["runtimeClasspath"].map { if (it.isDirectory) it else zipTree(it) })
}
