package view


import Utils.UIFor
import controller.MainController
import event.SearchEvent
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.control.*
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import model.Entry
import model.EntryModel
import model.EntryType
import tornadofx.*
import view.dialog.EntryCreationDialog
import view.dialog.EntryTypeChangeDialog
import view.dialog.EntryWindow


/**
 * Created by Michael on 22/08/2017.
 */
class ListTab : Fragment() {
    val controller : MainController by inject()
    val type : EntryType by param()
    override val root = hbox()
    var filter = SimpleStringProperty("")
    var sorter = SimpleStringProperty("name")
    var sortDirection = SimpleStringProperty("Ascending")
    var basedata = SimpleListProperty(controller.currentData.entries.filter { it.type == type }.observable()).apply {
        controller.currentData.entries.onChange {
            this.set(controller.currentData.entries.filter { it.type == type }.observable())
        }
    }
    var currentdata: SimpleListProperty<Entry> = SimpleListProperty(basedata.value).apply {
        /*this.bind(object: ListBinding<Entry>(){
            override fun computeValue(): ObservableList<Entry> = data.filter { filter.value in it.name }.sortedBy { it[sorter.value] }.toMutableList().observable()
        })*/
    }
    var colorblind = false
    val liststyle = "stylesheets/contentlist.css"
    val liststyle_colorblind = "stylesheets/contentlist_colorblind.css"
    val infopanelstyle = "stylesheets/infopanel.css"
    var infopane = VBox(10.0).also { it.padding = Insets(10.0) }
    //var list: ListView<Entry> by singleAssign()

    var model = EntryModel()


    init {
        with(root) {
            splitpane(Orientation.HORIZONTAL) {
                setDividerPositions(0.4)
                maxHeightProperty() + maxHeightProperty()
                prefWidthProperty().bind(FX.primaryStage.widthProperty())
                prefHeightProperty().bind(FX.primaryStage.heightProperty())
                //list on the left
                borderpane {
                    stylesheets += if (colorblind) liststyle_colorblind else liststyle
                    top = label(Bindings.concat("Displaying ").concat(currentdata.sizeProperty()).concat(" of ").concat(basedata.sizeProperty()).concat(" Elements"))
                    center = listview<Entry> {
                        itemsProperty().bind(currentdata)

                        selectionModel.selectionMode = SelectionMode.SINGLE
                        cellFormat {
                            graphic = cache{
                                label(itemProperty().select( Entry::nameProperty)){
                                    id = "entrylabel"
                                    contextmenu{
                                        item("change type").action {
                                            println("changing type of entry")
                                            find(EntryTypeChangeDialog::class, mapOf(EntryTypeChangeDialog::entry to selectedItem)).openModal(block = true)
                                        }
                                    }
                                }
                            }
                        }
                        bindSelected(model)
                        selectionModel.selectedItemProperty().onChange { refreshInfoPane() }
                    }
                    //center = list
                    bottom = stackpane {
                        button("Add new Entry") {
                            padding = insets(10, 10)
                            tooltip("add a new entry to this list")
                            action {
                                find(EntryCreationDialog::class, mapOf("initialType" to type)).openModal(block = true)
                            }
                        }
                    }

                }

                //information on the right
                scrollpane(fitToWidth = true) {
                    vbarPolicy = ScrollPane.ScrollBarPolicy.AS_NEEDED
                    content = infopane
                }
            }
        }
        subscribe<SearchEvent> {
            println("searching in " + type)
            filter.set(it.searchString)
            sorter.set(it.sortCriteria)
            sortDirection.set(it.sortDirection)
            currentdata.set(basedata
                    .filter { Utils.filter(filter.value,it) }
                    .sortedBy { it[sorter.value] }
                    .run { if (sortDirection.value == "Descending") this.asReversed() else this }
                    .toMutableList()
                    .observable())
        }
    }

    fun refreshInfoPane() {
        with(infopane) {
            clear()
            stylesheets += infopanelstyle
            if (model.isNotEmpty) {
                hbox(5) {
                    label("Name : ")
                    textfield(model.name)
                }

                hbox(5) {
                    label("Tags : ")
                    textfield(model.item.tags)
                }

                for ((key, value) in model.item.propertiesMap) {
                    hbox(10) {
                        label(key.capitalize() + " : "){
                            minWidth = Region.USE_PREF_SIZE
                            maxWidth = Region.USE_PREF_SIZE
                        }
                        UIFor(model.item.type.properties[key]!!,value)
                    }
                }

                stackpane {
                    hbox {
                        button("Save Entry") {
                            tooltip("Save the current shown entry")
                            action {
                                model.commit()
                                //list.refresh()
                            }
                        }

                        button("Delete Entry") {
                            tooltip("delete the current shown entry")
                            action {
                                basedata.remove(model.item)

                                //list.refresh()
                            }
                        }
                        button("Open in separate window") {
                            tooltip("show the current entry in a separate window")
                            action {
                                EntryWindow(model.item).show()
                            }
                        }
                    }
                }
            }else{
                label("Choose Entry")
            }
        }
    }

}