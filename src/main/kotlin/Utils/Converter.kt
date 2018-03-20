package Utils

import model.Entry
import model.LegacyType
import tornadofx.*
import java.time.LocalDate
import java.time.format.DateTimeParseException
import kotlin.reflect.KClass

inline fun String.toDateOrNull() = try { LocalDate.parse(this) }catch(e : DateTimeParseException){ null }

inline fun String.toIntOrZero() = this.toIntOrNull() ?: 0

fun String.convert() = toDateOrNull() ?: toIntOrNull() ?: toDoubleOrNull() ?: this

infix fun <T : Any> String.parseTo(type : KClass<T>) = when(type){
    Int::class -> this.toInt()
    Double::class -> this.toDouble()
    Long::class -> this.toLong()
    Boolean::class -> this.toBoolean()
    else -> this
} as T

fun Entry.asLegacy() : Entry {
    return Entry(this.name,this.tags, LegacyType).also {
        it["type"] = this.typeName
        it["rating"] = "0"
        it["experienced"] = "false"
        it["description"] = this.propertiesMap.toList().fold(""){ result , next ->
            result + next.first + ": " + next.second.value + " ; "
        }
    }
}