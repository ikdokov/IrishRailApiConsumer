package com.ikdokov.irishrailconsumer.data.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "objStation", strict = false)
data class Station @JvmOverloads constructor(

    @field:Element(name = "StationDesc")
    var desc: String = "",

    @field:Element(name = "StationAlias", required = false)
    var alias: String = "",

    @field:Element(name = "StationLatitude")
    var latitude: Double = 0.0,

    @field:Element(name = "StationLongitude")
    var longitude: Double = 0.0,

    @field:Element(name = "StationCode")
    var code: String = "",

    @field:Element(name = "StationId")
    var id: Long = 0
)