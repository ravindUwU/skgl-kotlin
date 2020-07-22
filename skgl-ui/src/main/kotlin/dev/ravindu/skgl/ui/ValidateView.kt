package dev.ravindu.skgl.ui

import javafx.geometry.Pos
import tornadofx.*

class ValidateController: Controller() {
}

class ValidateView: Fragment("Validate") {

	val controller: ValidateController by inject()

	override val root = vbox {

		style {
			alignment = Pos.CENTER
		}

		label("(Not implemented yet)")
	}
}
