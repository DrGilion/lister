package view.dialog

import Utils.UIFor
import javafx.scene.Scene
import javafx.stage.Stage
import model.Entry
import tornadofx.*

class EntryWindow(val entry : Entry) : Stage(){
    init {
        scene = Scene(vbox(5){
            hbox(10) {
                label("Name : ")
                textfield(entry.name)
            }

            hbox(10) {
                label("Tags : ")
                textfield(entry.tags)
            }

            for ((key, value) in entry.propertiesMap) {
                hbox(10) {
                    label(key.capitalize() + " : ")
                    UIFor(entry.type.properties[key]!!, value)
                }
            }
        })
    }
}