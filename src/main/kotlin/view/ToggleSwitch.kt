package view

import Utils.UIFor
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import tornadofx.*


class ToggleSwitch(initValue : Boolean) : HBox() {

    private lateinit var label : Label
    private lateinit var button : Button

    val switchedOn = SimpleBooleanProperty(initValue)

    init {
        with(this){
            label = label{
                setOnMousePressed { switchedOn.set(!switchedOn.get()) }
                prefWidthProperty().bind(this@with.widthProperty().divide(2))
                prefHeightProperty().bind(this@with.heightProperty())
            }
            button = button{
                setOnAction { switchedOn.set(!switchedOn.get()) }
                prefWidthProperty().bind(this@with.widthProperty().divide(2))
                prefHeightProperty().bind(this@with.heightProperty())
            }
        }

        setStyle()

        switchedOn.onChange { newVal ->
            if (newVal) {
                on()
            } else {
                off()
            }
        }
    }

    private fun on(){
        label.text = "YES"
        style = "-fx-background-color: green;"
        label.toFront()
    }

    private fun off(){
        label.text = "NO"
        style = "-fx-background-color: grey;"
        button.toFront()
    }

    private fun setStyle() {
        //Default Width
        width = 80.0
        label.alignment = Pos.CENTER
        style = "-fx-background-color: grey; -fx-text-fill:black; -fx-background-radius: 4;"
        alignment = Pos.CENTER_LEFT

        if(switchedOn.value) on()
        else off()
    }
}

fun EventTarget.toggleswitch(property : SimpleStringProperty) : ToggleSwitch{
    val toggle = ToggleSwitch(property.value.toBoolean())
    toggle.switchedOn.onChange {
        property.set(it.toString())
    }
    this += toggle
    return toggle
}