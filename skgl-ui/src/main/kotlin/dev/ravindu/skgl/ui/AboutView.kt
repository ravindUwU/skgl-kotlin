package dev.ravindu.skgl.ui

import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.*

class AboutView: Fragment("About") {

	override val root = vbox {

		style {
			alignment = Pos.CENTER
		}

		label(Constants.TITLE) {
			style {
				fontWeight = FontWeight.BOLD
				fontSize = 1.5.em
			}
		}
		label("Use this app to generate & validate serial keys generated via SKGL.")
		label("")
		hyperlink("https://github.com/RavinduL/skgl-kotlin")
		hyperlink("https://github.com/Cryptolens/SKGL")
	}
}
