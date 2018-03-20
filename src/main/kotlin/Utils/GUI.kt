package Utils

import javafx.beans.property.SimpleStringProperty
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.ComboBox
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import tornadofx.*
import view.TextFlowArea
import view.ToggleSwitch
import view.textflowarea
import view.toggleswitch

fun Parent.UIFor(type : String, prop : SimpleStringProperty) = when(type){
    "int" -> spinner<Int>(valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(Int.MIN_VALUE,Int.MAX_VALUE,prop.value.toIntOrZero())){
        isEditable = true
        this.valueProperty().onChange {
            prop.set(it.toString())
        }
    }
    "float" -> textfield(prop)
    "string" -> textflowarea(prop)
    "boolean" -> toggleswitch(prop)
    "date" -> datepicker {

    }
    else -> label("Kein passender Typ gefunden")
}

fun <T : Node> T.getResult() : String = when(this){
    is TextField -> this.text
    is TextArea -> this.text
    is ComboBox<*> -> this.selectionModel.selectedItem.toString()
    is ToggleSwitch -> this.switchedOn.get().toString()
    else -> this.toString()
}