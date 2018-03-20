package view

import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.stage.StageStyle
import tornadofx.*

class TextAnimation : Fragment() {
    override val root = stackpane {
        style(append = true) {
            borderColor += box(Color.BLACK)
            borderWidth = multi(box(5.px))
            backgroundColor = multi(Color.WHITE)
        }
        textLabel = text(params["text"] as String)
    }

    lateinit var textLabel : Text

    init {
        with(root){
            minHeightProperty().bind(currentStage?.minHeightProperty())
            minWidthProperty().bind(currentStage?.minWidthProperty())
        }
        with(currentStage!!){
            minHeight = 300.0
            minWidth = 400.0
        }

        timeline {
            keyframe(0.8.seconds){
                keyvalue(textLabel.scaleXProperty(), 3)
                keyvalue(textLabel.scaleYProperty(), 3)
            }
        }.setOnFinished { close() }
    }

}

fun Component.playTextAnimation(text : String) = find(TextAnimation::class,mapOf("text" to text)).openModal(stageStyle = StageStyle.UNDECORATED, block = true)

fun Component.playLoadedAnimation() = playTextAnimation("Data Loaded")

fun Component.playSavedAnimation() = playTextAnimation("Data Saved")