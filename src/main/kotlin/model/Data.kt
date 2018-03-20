package model

import tornadofx.*
import javax.json.Json
import javax.json.JsonObject

object Data : JsonModel {
    var entryTypes = arrayListOf<EntryType>().observable()
    var entries = arrayListOf<Entry>().observable()

    fun entryFor( name : String ) = entries.find { it.name == name }

    fun typeFor( typeName : String) = entryTypes.find { it.name.value == typeName }

    fun clear(){
        entries.clear()
        entryTypes.clear()
    }

    //+++++++++++++++++++++++++++SERIALIZATION+++++++++++++++++++++++++++

    override fun updateModel(json: JsonObject) {
        clear()
        with(json) {

            val loadedTypes = getJsonArray("types")
            for(type in loadedTypes.getValuesAs(JsonObject::class.java)){
                val newType = EntryType.empty.also { it.updateModel(type) }
                entryTypes.add(newType)
            }

            val loadedEntries = getJsonArray("data")
            for(entry in loadedEntries.getValuesAs(JsonObject::class.java)){
                val newEntry = Entry.empty.also { it.updateModel(entry) }
                this@Data.entries.add(newEntry)
            }
        }
    }

    override fun toJSON(json: JsonBuilder) {
        with(json) {
            val typesJSON = Json.createArrayBuilder()
            for (item in entryTypes) {
                typesJSON.add(item.toJSON())
            }
            add("types",typesJSON)

            val entriesJSON = Json.createArrayBuilder()
            for (item in entries) {
                entriesJSON.add(item.toJSON())
            }
            add("data",entriesJSON)
        }
    }
}