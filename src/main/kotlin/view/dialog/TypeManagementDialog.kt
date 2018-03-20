package view.dialog

import controller.MainController
import event.EntryTypesChangedEvent
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.SelectionMode
import model.EntryType
import model.EntryTypeModel
import tornadofx.*

class TypeManagementDialog : View("Manage Entry Types") {
    val controller: MainController by inject()
    val model = EntryTypeModel()
    val newPropertyName = SimpleStringProperty()
    val newPropertyType = SimpleStringProperty()
    override val root = borderpane()
    val formPane = Form()

    init {
        with(root){
            left {
                listview<EntryType> {
                    itemsProperty().bind(controller.currentData.entryTypes.toProperty())

                    selectionModel.selectionMode = SelectionMode.SINGLE
                    cellFormat {
                        graphic = label(it.name)
                    }
                    bindSelected(model)
                    selectionModel.selectedItemProperty().onChange { buildFormPane() }
                    selectionModel.selectFirst()

                }
            }
            center = formPane
            buildFormPane()
            bottom {
                buttonbar {
                    button("Save"){
                        action {
                            model.commit()
                            close()
                            fire(EntryTypesChangedEvent)
                        }
                    }
                    button("Cancel"){
                        action {
                            close()
                        }
                    }
                }
            }
        }
        with(currentStage!!){
            minHeight = 300.0
            minWidth = 400.0
            sizeToScene()
        }
    }

    fun buildFormPane(){
        with(formPane){
            clear()
            if(model.isNotEmpty) {
                fieldset("Properties for " + model.name.value + " Type") {
                    spacing = 5.0
                    for ((key, value) in model.item.properties) {
                        hbox(10) {
                            textfield(key)
                            label("of type " + value)
                        }
                    }
                }
                fieldset("Add additional properties") {
                    spacing = 5.0
                    hbox(20) {
                        field("Property Name:") { textfield(newPropertyName) }
                        field("Property Type:") {
                            combobox(property = newPropertyType,values = EntryType.possibleTypes) {
                                selectionModel.selectFirst()
                            }
                            button("Add new property") {
                                action {
                                    model.item[newPropertyName.value] = newPropertyType.value
                                    buildFormPane()
                                }
                            }
                        }
                    }
                }
            }else{
                label("Choose Type")
            }
        }
        currentStage?.sizeToScene()
    }
}