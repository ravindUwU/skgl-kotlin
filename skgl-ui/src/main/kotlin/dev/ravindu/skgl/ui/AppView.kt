package dev.ravindu.skgl.ui

import javafx.scene.control.TabPane
import tornadofx.View
import tornadofx.tabpane

class AppView: View("SKGL (Kotlin) UI") {

	override val root = tabpane {
		tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
		tab<GenerateView>()
	}
}
