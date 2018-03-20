package view

import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import tornadofx.action
import tornadofx.imageview
import tornadofx.item

/**
 * Created by Michael on 23/08/2017.
 */
fun javafx.scene.control.Menu.item2(name : String, graphicURL : String? = null, keycode : String? = null, op : () -> Unit) = item(name = name){
    if(graphicURL != null) graphic = imageview(Image(graphicURL, MENU_ICONSIZE.toDouble(),MENU_ICONSIZE.toDouble(),true,true))
    if(keycode != null) accelerator = KeyCodeCombination(KeyCode.getKeyCode(keycode), KeyCombination.CONTROL_DOWN)
    action { op() }
}