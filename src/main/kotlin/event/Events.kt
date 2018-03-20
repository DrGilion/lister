package event

import tornadofx.*
import model.Entry
import model.EntryType

class SearchEvent(val searchString : String,val sortCriteria: String, val sortDirection: String) : FXEvent()

class SortingCriteriaChangedEvent(val sortCriteria : String) : FXEvent()

class SortingDirectionChangedEvent(val sortDirection : String) : FXEvent()

class EntryTypeCreatedEvent(val type : EntryType) : FXEvent()

object EntryTypesChangedEvent : FXEvent()

class EntryTypeDeletedEvent(val type : EntryType) : FXEvent()

class EntryCreatedEvent(val entry : Entry) : FXEvent()

class EntryDeletedEvent(val entry: Entry) : FXEvent()

object DataLoadedEvent : FXEvent()

object ApplicationClosedEvent : FXEvent()