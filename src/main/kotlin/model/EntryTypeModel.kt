package model

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class EntryTypeModel : ItemViewModel<EntryType>() {
    val name : SimpleStringProperty = bind{item?.name}
}