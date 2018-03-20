package view.dialog

import model.Entry
import tornadofx.*

class EntryTypeChangeDialog : Fragment("Change the type of the entry") {
    val entry : Entry by param()
    override val root = borderpane {
        top = hbox {
            label("Name : ")
            textfield(entry.name)
        }
    }
}
