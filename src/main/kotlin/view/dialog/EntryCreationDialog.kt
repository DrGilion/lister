package view.dialog

import Utils.UIFor
import controller.MainController
import event.EntryCreatedEvent
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.paint.Color
import model.Entry
import model.EntryModel
import model.EntryType
import tornadofx.*
import view.ICONSIZE


class EntryCreationDialog : Fragment("Create a new entry") {
    override val root = borderpane()
    val controller : MainController by inject()
    val initialType : EntryType? = params["initialType"] as? EntryType
    val resultEntry = EntryModel().apply {
        item = Entry.empty
    }

    init {
        with(root){
            top = form {
                fieldset("Choose Name") {
                    field("Name: ") {
                        textfield(resultEntry.name)
                    }
                }
                fieldset("Add Tags") {
                    field("Tags: ") {
                        textfield(resultEntry.tags)
                        label(graphic = ImageView(Image("icons/info.png", ICONSIZE.toDouble(), ICONSIZE.toDouble(),true,true))){
                            tooltip("Tags are separated by commas")
                        }
                    }
                }
                fieldset("Choose Type") {
                    field("Type: ") {
                        combobox( values = controller.currentData.entryTypes){
                            cellFormat {
                                graphic = label(it.name){
                                    style {
                                        textFill = Color.BLACK
                                    }
                                }
                            }
                            selectionModel.selectedItemProperty().onChange {
                                resultEntry.item.type = it!!
                                buildForm()
                            }
                            selectionModel.select(initialType)

                        }
                    }
                }
            }
        }
    }

    private fun buildForm(){
        root.center = form {
            fieldset("Type Specific properties") {
                for ((key,value) in resultEntry.item.propertiesMap){
                    field("$key: ") {
                        UIFor(resultEntry.item.type.properties[key]!!,value)
                    }
                }
            }
        }
        root.bottom = buttonbar {
            button("Save"){
                action {
                    resultEntry.commit()
                    fire(EntryCreatedEvent(resultEntry.item))
                    close()
                }
            }
            button("Cancel"){
                action {
                    close()
                }
            }
        }
        currentStage?.sizeToScene()
    }
}