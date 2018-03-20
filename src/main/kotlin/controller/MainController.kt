package controller

import event.DataLoadedEvent
import event.EntryCreatedEvent
import event.EntryTypeCreatedEvent
import event.EntryTypesChangedEvent
import model.Data
import model.Entry
import model.EntryType
import tornadofx.*
import view.MainWindow
import java.io.File
import java.io.IOException
import java.io.ObjectInputStream
import java.util.*


/**
 * Created by Michael on 23/08/2017.
 */
class MainController : Controller() {

    var currentData = Data
    val mainWindow : MainWindow by inject()


    init {
        subscribe<EntryTypeCreatedEvent> {
            println(it.type)
            currentData.entryTypes.add(it.type)
            mainWindow.refreshTabs()
        }
        subscribe<EntryCreatedEvent> {
            currentData.entries.add(it.entry)
        }
        subscribe<EntryTypesChangedEvent> {
            //add, remove a property or do nothing for a given type

        }
    }
    //+++++++++++++++++++++++++++JSON+++++++++++++++++++++++++++++++

    fun saveEntriesAsJson(file: File) = currentData.save(file.outputStream())

    fun loadEntriesFromJson(file: File) {
        currentData.updateModel(loadJsonObject(file.inputStream()))
        fire(DataLoadedEvent)
    }

    //++++++++++++++++++++++++++LEGACY++++++++++++++++++++++++++++++

    fun loadLegacyFile(file : File){
        try {
            val ois = ObjectInputStream(file.inputStream())
            data.Entry.setEntryMap(ois.readObject() as HashMap<String, ArrayList<data.Entry>>)
            ois.close()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        } catch (c: ClassNotFoundException) {
            println("Class not found")
            c.printStackTrace()
        }

        //import Legacy Entries
        currentData.clear()

        //currentData.entryTypes.add(LegacyType)
        for(type in data.Entry.types()){
            currentData.entryTypes.add(EntryType(type).apply {
                this["rating"] = "int"
                this["experienced"] = "boolean"
                this["description"] = "string"
            })
        }
        for(list in data.Entry.getEntryMap().values){
            for (entry in list){
                currentData.entries.add(Entry(name = entry.title, initialType = currentData.typeFor(entry.type) ?: EntryType.empty).apply {
                    this["rating"] = entry.rating.ordinal.toString()
                    this["experienced"] = entry.hasExperienced().toString()
                    this["description"] = entry.description
                })
            }
        }
        mainWindow.refreshTabs()
    }

}