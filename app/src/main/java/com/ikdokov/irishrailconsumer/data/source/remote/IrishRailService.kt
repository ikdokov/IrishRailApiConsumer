package com.ikdokov.irishrailconsumer.data.source.remote

import com.ikdokov.irishrailconsumer.data.model.StationDataList
import com.ikdokov.irishrailconsumer.data.model.StationList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

public interface IrishRailService {

    @GET("/realtime/realtime.asmx/getAllStationsXML")
    fun getAllStations(): Call<StationList>

    @GET("/realtime/realtime.asmx/getStationDataByNameXML")
    fun getStationData(@Query("StationDesc") stationDesc: String): Call<StationDataList>
}