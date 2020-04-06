package com.ikdokov.irishrailconsumer.data.model

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "ArrayOfObjStationData", strict = false)
data class StationDataList @JvmOverloads constructor(
    @field:ElementList(name = "objStationData", inline = true, required = false)
    var dataList: List<StationData> = mutableListOf()
)