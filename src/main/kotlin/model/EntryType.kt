package model

import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.json.Json
import javax.json.JsonObject
import javax.json.JsonString

/**
 * Created by Michael on 21/08/2017.
 */
class EntryType(name : String) : JsonModel{
    companion object {
        val empty : EntryType
            get() = EntryType("unnamed")

        val possibleTypes = listOf("string","int" ,"float" ,"boolean","date" )
    }

    val name = SimpleStringProperty(this,"name",name)

    var properties = mutableMapOf<String, String>()

    operator fun contains(prop: String) = prop == "name" || properties.containsKey(prop)

    operator fun get(property : String) = when(property){
        "name" -> name.value
        else -> properties[property]
    }

    operator fun set(propertyName : String, typeName : String){
        when(propertyName){
            "name" -> name.value = typeName
            else -> properties.put(propertyName, typeName)
        }
    }

    override fun toString() = toJSON().toString()

    //+++++++++++++++++++++++++++SERIALIZATION+++++++++++++++++++++++++++

    override fun updateModel(json: JsonObject) {
        with(json) {
            this@EntryType["name"] = string("name") ?: "Typename" + System.currentTimeMillis()
            for ((key, value) in getJsonObject("properties")){
                this@EntryType[key] = (value as JsonString).string
            }
        }
    }

    override fun toJSON(json: JsonBuilder) {
        with(json) {
            add("name", name)
            val props = Json.createObjectBuilder()
            for( (key,type) in properties){
                props.add(key,type)
            }
            add("properties",props)
        }
    }
}