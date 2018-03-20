package view.dialog

import controller.MainController
import model.*
import event.EntryTypeCreatedEvent
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import tornadofx.*

class ListCreationDialog : Fragment() {
    val controller: MainController by inject()
    override val root = borderpane()
    lateinit var nameField : TextField
    lateinit var propField : ComboBox<String>
    val model = EntryTypeModel().apply {
        item = EntryType.Companion.empty
    }

    init {
        with(root){
            top = form {
                fieldset("Choose Name") {
                    field("Name: ") {
                        textfield(model.name)
                    }
                }
            }
            rebuildPane()
            bottom = buttonbar {
                button("Save") {
                    action {
                        model.commit()
                        fire(EntryTypeCreatedEvent(model.item))
                        close()
                    }
                }
                button("Cancel") {
                    action {
                        close()
                    }
                }
            }
        }
    }

    fun rebuildPane() {
        with(root) {
            center = form {
                fieldset("Properties for " + model.name.value + " Type") {
                    spacing = 5.0
                    for ((key, value) in model.item.properties) {
                        hbox(10) {
                            textfield(key)
                            label("of type " + value)
                        }
                    }
                }
                fieldset {
                    hbox(20) {
                        field("Property Name:") { nameField = textfield() }
                        field("Property Type:") {
                            propField = combobox(values = EntryType.possibleTypes) {
                                selectionModel.selectFirst()
                                cellFormat {
                                    graphic = label(it)
                                }
                            }
                            button("Add new property") {
                                action {
                                    model.item.properties.put(nameField.text,propField.selectionModel.selectedItem)
                                    rebuildPane()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}