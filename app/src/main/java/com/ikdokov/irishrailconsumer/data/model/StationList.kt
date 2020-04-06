package com.ikdokov.irishrailconsumer.data.model

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "ArrayOfObjStation", strict = false)
data class StationList @JvmOverloads constructor(
    @field:ElementList(name = "objStation", inline = true, required = false)
    var stationsList: List<Station> = mutableListOf()
)