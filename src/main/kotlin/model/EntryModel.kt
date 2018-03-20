package model

import javafx.beans.property.*
import javafx.collections.FXCollections
import tornadofx.*

class EntryModel : ItemViewModel<Entry>() {
    val name : SimpleStringProperty = bind{ item?.nameProperty}
    val tags : SimpleStringProperty = bind{ item?.tagsProperty}
    val entryProperties = FXCollections.observableHashMap<String, SimpleStringProperty>().apply {
        if (this@EntryModel.isNotEmpty)
        for ( (key,value)  in item.propertiesMap){
            this.put(key,this@EntryModel.bind{value})
        }
    }
}

