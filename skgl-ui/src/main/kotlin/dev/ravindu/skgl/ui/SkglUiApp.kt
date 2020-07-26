package dev.ravindu.skgl.ui

import javafx.stage.Stage
import tornadofx.App
import tornadofx.launch

class SkglUiApp: App(AppView::class) {

	override fun start(stage: Stage) {
		stage.apply {
			isResizable = true
		}
		super.start(stage)
	}

	companion object {

		@JvmStatic
		fun main(args: Array<String>) {
			launch<SkglUiApp>(args)
		}
	}
}
