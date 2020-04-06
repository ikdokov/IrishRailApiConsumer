package com.ikdokov.irishrailconsumer.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikdokov.irishrailconsumer.data.model.*
import com.ikdokov.irishrailconsumer.data.source.remote.IrishRailService
import com.ikdokov.irishrailconsumer.ui.model.StationUiModel
import com.ikdokov.irishrailconsumer.ui.model.TrainUiModel
import okhttp3.OkHttpClient
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.transform.RegistryMatcher
import org.simpleframework.xml.transform.Transform
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.text.SimpleDateFormat
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

object IrishRailRepository {

    private val TAG = IrishRailRepository.javaClass.name
    private var service = initRetrofit().create<IrishRailService>(IrishRailService::class.java)
    private const val DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.S"

    fun getAllStations(): LiveData<Resource<List<StationUiModel>>> {
        val result = MutableLiveData<Resource<List<StationUiModel>>>()
        result.value = Resource.loading()

        service.getAllStations().enqueue(object : Callback<StationList> {
            override fun onFailure(call: Call<StationList>, t: Throwable) {
                result.value = Resource.error()
                Log.e(TAG, t.message)
            }

            override fun onResponse(call: Call<StationList>, response: Response<StationList>) {
                response.body()!!.stationsList.map {
                    StationUiModel(it.desc)
                }.also {
                    result.value = Resource.success(it)
                }
            }
        })

        return result
    }

    fun getTrainList(stationName: String): LiveData<Resource<List<TrainUiModel>>> {
        val result = MutableLiveData<Resource<List<TrainUiModel>>>()
        result.value = Resource.loading()

        service.getStationData(stationName).enqueue(object : Callback<StationDataList> {
            override fun onFailure(call: Call<StationDataList>, t: Throwable) {
                result.value = Resource.error()
                Log.e(TAG, t.message)
            }

            override fun onResponse(
                call: Call<StationDataList>,
                response: Response<StationDataList>
            ) {
                response.body()!!.dataList.map {
                    TrainUiModel(
                        it.expArrival, it.trainCode, it.destination, determineMinutesLeft(
                            it.serverTime,
                            it.expArrival
                        )
                    )
                }.sortedBy { it.arivalTimeLeft }.also {
                    result.value = Resource.success(it)
                }
            }
        })

        return result
    }

    private fun determineMinutesLeft(serverTime: Date, expArrival: String): Long {
        val hour = expArrival.substringBefore(":", "0").toInt()
        val minute = expArrival.substringAfter(":", "0").toInt()
        val expArrivalTime = Calendar.getInstance().apply {
            time = serverTime
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            if (serverTime.hours > 12 && expArrival.startsWith('0')) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        return ChronoUnit.MINUTES.between(
            serverTime.toInstant(),
            expArrivalTime.toInstant()
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun initRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://api.irishrail.ie/")
            .client(provideHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(SimpleXmlConverterFactory.create(providePersister()))
            .build()
    }

    private fun provideHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private fun providePersister(): Persister {
        return Persister(RegistryMatcher().apply {
            bind(Date::class.java, DateTransformer(SimpleDateFormat(DATE_TIME_PATTERN)))
        })
    }

    private class DateTransformer(private val format: SimpleDateFormat) : Transform<Date> {
        override fun write(value: Date?): String {
            return format.format(value)
        }

        override fun read(value: String?): Date {
            return format.parse(value)
        }
    }
}