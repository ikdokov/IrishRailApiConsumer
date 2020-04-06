package com.ikdokov.irishrailconsumer.data.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.util.*

@Root(name = "objStationData", strict = false)
data class StationData @JvmOverloads constructor(

    @field:Element(name = "Servertime")
    var serverTime: Date = Date(),

    @field:Element(name = "Traincode")
    var trainCode: String = "",

    @field:Element(name = "Stationfullname")
    var stationFullName: String = "",

    @field:Element(name = "Stationcode")
    var stationCode: String = "",

    @field:Element(name = "Origin")
    var origin: String = "",

    @field:Element(name = "Destination")
    var destination: String = "",

    @field:Element(name = "Origintime")
    var originTime: String = "",

    @field:Element(name = "Destinationtime")
    var destinationTime: String = "",

    @field:Element(name = "Exparrival")
    var expArrival: String = "",

    @field:Element(name = "Expdepart")
    var expDepart: String = "",

    @field:Element(name = "Scharrival")
    var schArrival: String = "",

    @field:Element(name = "Schdepart")
    var schDepart: String = "",

    @field:Element(name = "Direction")
    var direction: String = "",

    @field:Element(name = "Traintype")
    var trainType: String = "",

    @field:Element(name = "Locationtype")
    var locationType: String = ""
)