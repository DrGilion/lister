package model

/**
 * Created by Michael on 21/08/2017.
 */

val LegacyType = EntryType("Legacy").apply {
    this.properties["category"] = "string"
    this.properties["rating"] = "int"
    this.properties["experienced"] = "boolean"
    this.properties["description"] = "string"
}

val MovieType = EntryType("Movie").apply {
    this.properties["year"] = "int"
}

val FoodType = EntryType("Food").apply {
    this.properties["course"] = "string"
    this.properties["rating"] = "int"
}