package com.ikdokov.irishrailconsumer.ui.station

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ikdokov.irishrailconsumer.data.IrishRailRepository
import com.ikdokov.irishrailconsumer.data.model.Resource
import com.ikdokov.irishrailconsumer.ui.model.StationUiModel

class StationSearchViewModel : ViewModel() {

    fun getAllStations() : LiveData<Resource<List<StationUiModel>>> {
        return IrishRailRepository.getAllStations()
    }
}
