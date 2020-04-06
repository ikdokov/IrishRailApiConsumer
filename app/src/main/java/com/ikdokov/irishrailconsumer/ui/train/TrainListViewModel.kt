package com.ikdokov.irishrailconsumer.ui.train

import androidx.lifecycle.*
import com.ikdokov.irishrailconsumer.data.IrishRailRepository
import com.ikdokov.irishrailconsumer.data.model.Resource
import com.ikdokov.irishrailconsumer.ui.model.TrainUiModel

class TrainListViewModel() : ViewModel() {

    fun getTrainList(stationName: String): LiveData<Resource<List<TrainUiModel>>> {
        return IrishRailRepository.getTrainList(stationName)
    }
}

