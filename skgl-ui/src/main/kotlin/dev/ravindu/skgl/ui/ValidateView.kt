package dev.ravindu.skgl.ui

import dev.ravindu.skgl.SerialKey
import javafx.beans.binding.Bindings
import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.geometry.Pos
import tornadofx.*
import java.time.LocalDate
import java.util.concurrent.Callable

class ValidateController: Controller() {

	val keyProperty = SimpleStringProperty("")
	val secretProperty = SimpleStringProperty("")

	val isValidProperty = SimpleBooleanProperty(true)
	val messageProperty = SimpleStringProperty("Enter the serial key & secret via the controls on the left an hit \"Validate\"")

	val durationProperty = SimpleIntegerProperty()
	val createdOnProperty = SimpleObjectProperty<LocalDate>()
	val expiresOnProperty = SimpleObjectProperty<LocalDate>()
	val daysLeftProperty = SimpleIntegerProperty()
	val isExpiredProperty = SimpleBooleanProperty()
	val featuresProperty = SimpleSetProperty<Int>(FXCollections.observableSet())

	fun decode() {

		try {
			val serial = SerialKey(keyProperty.value, secretProperty.value)
			durationProperty.value = serial.duration
			createdOnProperty.value = serial.createdOn
			expiresOnProperty.value = serial.expiresOn
			daysLeftProperty.value = serial.calculateDaysLeft()
			isExpiredProperty.value = serial.calculateIsExpired()

			featuresProperty.clear()
			featuresProperty.addAll(serial.features)

			messageProperty.value = "The specified key is valid"
			isValidProperty.value = true

		} catch (e: Exception) {
			isValidProperty.value = false
			messageProperty.value = "${e::class.simpleName}: ${e.message}"
		}
	}
}

class ValidateView: Fragment("Validate") {

	val controller: ValidateController by inject()

	override val root = hbox {

		form {

			fieldset {

				field("Key") {
					textfield {
						bind(controller.keyProperty)
					}
				}

				field("Secret") {
					textfield {
						bind(controller.secretProperty)
					}
				}

				field {
					borderpane {
						right = button("Validate") {
							action {
								controller.decode()
							}
						}
					}
				}
			}
		}

		form {

			imageview("check.png") {
				alignment = Pos.TOP_CENTER
				scaleX = 0.5
				scaleY = 0.5
				visibleProperty().bind(controller.isValidProperty)
				managedProperty().bind(controller.isValidProperty)
			}

			imageview("close.png") {
				alignment = Pos.TOP_CENTER
				scaleX = 0.5
				scaleY = 0.5
				visibleProperty().bind(controller.isValidProperty.not())
				managedProperty().bind(controller.isValidProperty.not())
			}

			label {
				bind(controller.messageProperty)
			}

			fieldset {
				visibleProperty().bind(controller.isValidProperty)
				managedProperty().bind(controller.isValidProperty)

				field("Duration") {
					textfield {
						bind(controller.durationProperty)
					}
					label("days")
				}

				field("Created on") {
					datepicker {
						bind(controller.createdOnProperty)
						isEditable = false
					}
				}

				field("Expires on") {
					datepicker {
						bind(controller.expiresOnProperty)
						isEditable = false
					}
				}

				field("Days left") {
					textfield {
						bind(controller.daysLeftProperty)
					}
					label("days")
				}

				field("Is expired") {
					checkbox {
						bind(controller.isExpiredProperty)
					}
				}

				field("Features") {
					for (i in 1..8) {
						togglebutton(i.toString(), selectFirst = false) {
							selectedProperty().bind(Bindings.createBooleanBinding(Callable { controller.featuresProperty.contains(i) }, controller.featuresProperty))
						}
					}
				}
			}
		}
	}
}
