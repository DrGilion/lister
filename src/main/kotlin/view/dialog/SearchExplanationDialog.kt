package view.dialog

import tornadofx.*

class SearchExplanationDialog : View("") {
    override val root = borderpane()

    init {
        root.center = hbox {
            label("""
                The search can be done normally or advanced.
                The basic search finds all entries which match the given search terms.
                By ticking the checkbox "advanced" beside the search field you can use a more sophisticated search functionality.
                You can now use a SQL-like syntax to match keywords to specific properties in a category and use logical operators (and, or, xor, like and equivalent symbols) e.g:
                name = Burger and ingredients contains "Cheddar"
                """)
        }
    }
}
