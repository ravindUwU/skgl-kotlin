package dev.ravindu.skgl.ui

import dev.ravindu.skgl.SerialKey
import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.scene.layout.Priority
import tornadofx.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class GenerateController: Controller() {

	val secretProperty = SimpleStringProperty("")
	val expiryDateProperty = SimpleObjectProperty<LocalDate>(LocalDate.now().plusDays(7))
	val chunkedProperty = SimpleBooleanProperty(true)
	val keyCountProperty = SimpleIntegerProperty(1)
	val featuresProperty = SimpleSetProperty<Int>(FXCollections.observableSet())

	val resultTextProperty = SimpleStringProperty("")

	fun generate() {
		resultTextProperty.set(
			(1..keyCountProperty.value).joinToString("\n") {
				SerialKey.build(secretProperty.value) {
					features = featuresProperty
					duration = LocalDate.now().until(expiryDateProperty.value, ChronoUnit.DAYS).toInt()
					chunk = chunkedProperty.value
				}.text
			}
		)
	}
}

class GenerateView: Fragment("Generate") {

	val controller: GenerateController by inject()

	override val root = hbox {

		form {

			fieldset {

				field("Secret") {
					textfield {
						bind(controller.secretProperty)
					}
				}

				field("Expiry date") {
					datepicker {
						bind(controller.expiryDateProperty)
						useMaxWidth = true
					}
				}

				field("Features") {
					for (i in 1..8) {
						togglebutton(i.toString(), selectFirst = false) {
							action {
								if (isSelected) {
									controller.featuresProperty.add(i)
								} else {
									controller.featuresProperty.remove(i)
								}
							}
						}
					}
				}

				field ("# of keys") {
					textfield {
						bind(controller.keyCountProperty)
						filterInput { it.controlNewText.isInt() }
					}
				}

				field ("Options") {
					checkbox("Chunked") {
						bind(controller.chunkedProperty)
					}
				}

				field {
					borderpane {
						right = button("Generate") {
							action {
								controller.generate()
							}
						}
					}
				}
			}
		}

		borderpane {

			style {
				padding = box(1.em)
			}

			center = textarea {
				bind(controller.resultTextProperty)
				vgrow = Priority.NEVER
				promptText = "Configure key generation via the options on the left and hit \"Generate\""
				isEditable = false
				style {
					fontFamily = "monospaced"
				}
			}
		}
	}
}
