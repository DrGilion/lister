package model

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import tornadofx.*
import javax.json.Json
import javax.json.JsonObject
import javax.json.JsonString

/**
 * Created by Michael on 21/08/2017.
 */
class Entry(name : String = "unnamed", tags: String = "", initialType: EntryType) : JsonModel{
    companion object {
        val empty : Entry
        get() = Entry("","",EntryType(""))
    }

    var nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    var tagsProperty = SimpleStringProperty(this, "tags", tags)
    var tags by tagsProperty

    var propertiesMap = FXCollections.observableHashMap<String, SimpleStringProperty>()

    var type: EntryType = initialType
    set(value) {
        field = value
        setupPropertiesMap()
    }

    val typeName : String
    get() = this.type.name.value

    init {
        setupPropertiesMap()
    }

    fun setupPropertiesMap(){
        with(propertiesMap) {
            clear()
            for ( key in type.properties.keys){
                this.put(key,SimpleStringProperty(this@Entry, key, ""))
            }
        }
    }

    operator fun contains(prop: String) = prop == "name" || prop == "tags" || propertiesMap.containsKey(prop)

    operator fun get(property : String) = when(property){
        "name" -> name
        "tags" -> tags
        else -> propertiesMap[property]?.value
    }

    operator fun set(propertyName : String, property : String){
        when(propertyName){
            "name" -> name = property
            "tags" -> tags = property
            else -> propertiesMap.put(propertyName, SimpleStringProperty(property))
        }
    }

    fun changeType(newType : EntryType){
        println("changing from ${type.name} to ${newType.name}")
        type = newType
    }

    override fun toString() = toJSON().toString()


    //+++++++++++++++++++++++++++SERIALIZATION+++++++++++++++++++++++++++

    override fun updateModel(json: JsonObject) {
        with(json) {
            name = string("name")
            tags = string("tags") ?: ""
            type = Data.typeFor(string("type")!!) ?: EntryType.empty
            for ((key, value) in getJsonObject("properties")){
                this@Entry[key] = (value as JsonString).string
            }
        }
    }

    override fun toJSON(json: JsonBuilder) {
        with(json) {
            add("name", name)
            add("tags", tags ?: "")
            add("type",type.name)
            val props = Json.createObjectBuilder()
            for( (key,type) in propertiesMap){
                props.add(key,type.value)
            }
            add("properties",props)
        }
    }
}

