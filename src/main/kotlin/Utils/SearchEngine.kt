package Utils

import model.Entry

val keywords = arrayListOf("and","&","or","|","xor","^","not","!","<>","eq","=","=/=","in","<","<=",">",">=")

fun filter(filter : String, entry : Entry) : Boolean {
    val filters = filter.split(" ")
    return filters in entry.name.toLowerCase() ||
            filters in entry.tags.toLowerCase() ||
            entry.propertiesMap.values.any { filters in it.value.toLowerCase() }
}

operator fun String.contains(filters : List<String>) = filters.any { it.toLowerCase() in this }
