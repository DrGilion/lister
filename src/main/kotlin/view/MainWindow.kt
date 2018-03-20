package view

import Utils.convert
import controller.MainController
import event.DataLoadedEvent
import event.SearchEvent
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.CheckMenuItem
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.stage.FileChooser
import tornadofx.*
import view.dialog.*
import java.awt.Toolkit


/**
 * Created by Michael on 21/08/2017.
 */


class MainWindow : View() {
    override val root = borderpane()
    val controller : MainController by inject()

    var searchString = SimpleStringProperty("")
    var sortString = SimpleStringProperty()
    var sortDirectionString = SimpleStringProperty()
    var groupByString = SimpleStringProperty()

    var advancedSearch = SimpleBooleanProperty(false)


    init{
        with(root) {
            title = "To-Do-Lister " + VERSION
            //setStageIcon(Image("icons/list.png"))
            val screenWidth = Toolkit.getDefaultToolkit().screenSize.getWidth()
            val screenHeight = Toolkit.getDefaultToolkit().screenSize.getHeight()
            setWindowMinSize(screenWidth /2, screenHeight /2)
            FX.primaryStage.height = screenHeight/2
            FX.primaryStage.width = screenWidth/2
            FX.primaryStage.setOnCloseRequest { playTextAnimation("Bye") }

            top = vbox {
                menubar {
                    menu("File") {
                        item2("Load...", "icons/file_open.png", "L"){
                            val file = chooseFile(filters = arrayOf(
                                    FileChooser.ExtensionFilter("All Formats","*.*"),
                                    FileChooser.ExtensionFilter("JSON","*.json"),
                                    FileChooser.ExtensionFilter("XML","*.xml"),
                                    FileChooser.ExtensionFilter("Legacy Files","*.list")
                            )).firstOrNull()
                            if (file != null) {
                                if(file.name.endsWith("list")){
                                    controller.loadLegacyFile(file)
                                }else{
                                    controller.loadEntriesFromJson(file)
                                }
                                playLoadedAnimation()
                            }else{
                                playTextAnimation("Loading Failed")
                            }

                            refreshTabs()
                        }
                        item2("Save", "icons/save.png", "S"){
                            val file = chooseFile(filters = arrayOf(
                                    FileChooser.ExtensionFilter("All Formats","*.*"),
                                    FileChooser.ExtensionFilter("JSON","*.json")
                            ),mode = FileChooserMode.Save).firstOrNull()
                            if (file != null) {
                                controller.saveEntriesAsJson(file)
                                playSavedAnimation()
                            }else{
                                playTextAnimation("Saving Failed")
                            }


                        }
                        item2("Save As"){ println("Save as") }
                        item2("Close", "icons/exit.png"){ FX.primaryStage.close()}
                    }
                    menu("Actions"){
                        item2("Add new list", "icons/tab_add.png"){ find(ListCreationDialog::class).openModal(block = true)}
                        item2("Add new entry", "icons/entry_add.png"){ find(EntryCreationDialog::class).openModal(block = true)}
                        item2("Manage Types", "icons/entry_add.png"){ find(TypeManagementDialog::class).openModal(block = true) }
                    }
                    menu("Options") {
                        item2("Settings", "icons/info.png"){ openInternalWindow(SettingsDialog::class)}
                        menu("Saving Formats") {
                            checkmenuitem("XML")
                            checkmenuitem("JSON")
                            checkmenuitem("SQLite")
                            checkmenuitem("TXT")
                            item("check all formats"){
                                action {
                                    parentMenu.items.filtered { it is CheckMenuItem }.forEach { (it as CheckMenuItem).isSelected = true }
                                }
                            }
                        }
                    }
                    menu("Help") {
                        item2("About the search function","icons/info.png"){ find(SearchExplanationDialog::class).openModal(block = true) }
                        item2("About this application","icons/info.png"){ aboutDialog() }
                    }

                }

                toolbar {
                    hbox(5) {
                        alignment = Pos.CENTER
                        label("Search: ")
                        textfield(searchString) {
                            promptText = "Search"
                            textProperty().onChange {
                                println("$text : "+ text.convert()::class)
                            }
                            setOnKeyPressed {
                                if(it.code == KeyCode.ENTER){ fire(SearchEvent(this.text,sortString.value, sortDirectionString.value)) }
                            }
                        }
                        checkbox(text = "(advanced)", property = advancedSearch)
                    }
                    hbox(5) {
                        alignment = Pos.CENTER
                        label("Sort by:")
                        val combo1 = combobox(sortString, arrayListOf("name","tags") ){
                            selectionModel.selectFirst()
                            selectionModel.selectedItemProperty().onChange { fire(SearchEvent(searchString.value, sortString.value, sortDirectionString.value)) }
                        }
                        combobox(sortDirectionString, listOf("Ascending","Descending")){
                            selectionModel.selectFirst()
                            selectionModel.selectedItemProperty().onChange { fire(SearchEvent(searchString.value,sortString.value, sortDirectionString.value)) }
                        }
                        label("Group by:")
                        val combo3 = combobox(groupByString, arrayListOf("name","tags")){
                            selectionModel.selectFirst()
                            selectionModel.selectedItemProperty().onChange { fire(SearchEvent(searchString.value,sortString.value, sortDirectionString.value)) }
                        }
                        subscribe<DataLoadedEvent> {
                            combo1.items.setAll(arrayListOf("name","tags") + controller.currentData.entryTypes.flatMap { it.properties.keys }.distinct())
                            combo3.items.setAll(arrayListOf("name","tags") + controller.currentData.entryTypes.flatMap { it.properties.keys }.distinct())
                        }
                    }
                }
            }
        }

    }

    fun refreshTabs(){
        root.center = tabpane {

            // tabs are added here
            for ( type in controller.currentData.entryTypes){
                tab {
                    graphic = label(type.name)
                    content = find<ListTab>(mapOf(ListTab::type to type)).root
                }
            }
        }
    }

}