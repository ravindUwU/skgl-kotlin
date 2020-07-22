package dev.ravindu.skgl.ui

import javafx.scene.control.TabPane
import tornadofx.View
import tornadofx.tabpane

class AppView: View(Constants.TITLE) {

	override val root = tabpane {
		tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

		tab<AboutView>()
		tab<GenerateView>()
		tab<ValidateView>()
	}
}
