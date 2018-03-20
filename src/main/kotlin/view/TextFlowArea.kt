package view

import com.sun.javafx.scene.control.skin.TextAreaSkin
import javafx.beans.binding.IntegerBinding
import javafx.beans.property.SimpleStringProperty
import javafx.event.EventTarget
import javafx.scene.control.*
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import tornadofx.*
import java.util.regex.Pattern


class TextFlowArea(val property : SimpleStringProperty) : StackPane(){
    val urlPattern =  Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
    val textflow = TextFlow()
    val textarea = TextArea().apply{
        bind(property)
        isWrapText = true
        prefRowCountProperty().bind(object : IntegerBinding(){
            override fun computeValue() = text.count { it == '\n'  } + 3
        })
        //textProperty().onChange { prefRowCount = text.count { it == '\n'  } +1 }
        skin = object : TextAreaSkin(this) {
            override fun populateContextMenu(contextMenu: ContextMenu?) {
                super.populateContextMenu(contextMenu)
                contextMenu?.items?.add(MenuItem("Save").apply { action { viewMode() } })
            }
        }
    }

    init {
        if(property.value.isBlank()) editMode() else viewMode()
        textflow.onDoubleClick {
            editMode()
        }
    }

    private fun editMode(){
        clear()
        this += textarea
    }

    private fun viewMode(){
        with(textflow){
            this@TextFlowArea.clear()
            this@TextFlowArea += this
            clear()
            /*val indexList = arrayListOf<Pair<Int,Int>>()
            for ( word in property.value.split(" ")){
                if(word.matches(urlPattern.toRegex())) indexList += property.value.indexOf(word) to property.value.indexOf(word)+word.length-1
            }
            for ( (iteration, indices) in indexList.withIndex()){
                if(indices.first == 0) hyperlink(property.value.substring(indices.first..indices.second))

            }
            val stringList = arrayListOf<String>()
            var normalText = true
            for ( word in property.value.split("\\s+".toRegex())){
                if(word.matches(urlPattern.toRegex())){
                    this.children.add(Hyperlink(word))
                }else{
                    this.children.add(Text(word + " "))
                }
            }*/
            this += Text(property.value)
        }
    }
}

fun EventTarget.textflowarea(property : SimpleStringProperty, op: (TextFlowArea.() -> Unit)? = null) = opcr(this, TextFlowArea(property), op)