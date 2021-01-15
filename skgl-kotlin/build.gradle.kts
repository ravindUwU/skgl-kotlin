repositories {
	jcenter()
}

plugins {
	`maven-publish`
	kotlin("jvm")
	id("org.jetbrains.dokka") version "1.4.10.2"
}

java {
	withSourcesJar()
}

publishing {

	publications {

		create<MavenPublication>("skgl-kotlin") {

			pom {
				name.set("SKGL (Kotlin)")
				description.set("Generate & validate human readable, 20-character serial keys with up to 8 features embedded")
				url.set("https://github.com/RavinduL/skgl-kotlin")

				licenses {
					license {
						name.set("BSD-3-Clause License")
						url.set("https://github.com/RavinduL/skgl-kotlin/blob/master/LICENSE")
					}
				}

				developers {
					developer {
						id.set("RavinduL")
						name.set("Ravindu Liyanapathirana")
						email.set("l.ravindu@hotmail.com")
					}
				}

				scm {
					connection.set("scm:git:git://github.com/RavinduL/skgl-kotlin.git")
					developerConnection.set("scm:git:ssh://github.com/RavinduL/skgl-kotlin.git")
					url.set("https://github.com/RavinduL/skgl-kotlin/tree/master")
				}
			}
		}
	}
}
