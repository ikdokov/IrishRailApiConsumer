package com.ikdokov.irishrailconsumer.ui.train

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikdokov.irishrailconsumer.R
import com.ikdokov.irishrailconsumer.util.SharedPrefUtils
import com.ikdokov.irishrailconsumer.data.model.Resource
import com.ikdokov.irishrailconsumer.data.model.Resource.Companion.STATUS_ERROR
import com.ikdokov.irishrailconsumer.data.model.Resource.Companion.STATUS_LOADING
import com.ikdokov.irishrailconsumer.data.model.Resource.Companion.STATUS_SUCCESS
import com.ikdokov.irishrailconsumer.ui.model.TrainUiModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.progress_bar
import kotlinx.android.synthetic.main.fragment_main.rv_station_list
import kotlinx.android.synthetic.main.fragment_main.toolbar

class TrainListFragment : Fragment() {

    private lateinit var currentStation: String

    private val trainListObserver = Observer<Resource<List<TrainUiModel>>> { stationListResource ->
        when (stationListResource.status) {
            STATUS_SUCCESS -> {
                progress_bar.visibility = View.GONE
                stationListResource.data?.let { stations ->
                    handleGetTrainListSuccess(stations)
                }
            }

            STATUS_ERROR -> {
                handleGetTrainListStatusError()
            }

            STATUS_LOADING -> {
                if (!swipe_container.isRefreshing) {
                    progress_bar.visibility = View.VISIBLE
                }
            }
        }
    }

    private val viewModel: TrainListViewModel by lazy {
        ViewModelProvider(this@TrainListFragment).get(TrainListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setTitle(R.string.main_fragment_title)

        handleSelectedStation()

        btn_pick_station.setOnClickListener {
            findNavController().navigate(TrainListFragmentDirections.actionMainFragmentToStationSearchFragment())
        }

        swipe_container.setOnRefreshListener {
            viewModel.getTrainList(currentStation).observe(viewLifecycleOwner, trainListObserver)
        }
    }

    private fun handleSelectedStation() {
        val selectedStation = arguments?.getString("station")

        currentStation = if (selectedStation.isNullOrEmpty()) {
            SharedPrefUtils.getStoredStation(activity)
        } else {
            SharedPrefUtils.storeSelectedStation(activity, selectedStation)
            selectedStation
        }

        if (currentStation.isNotEmpty()) {
            btn_pick_station.text = currentStation
            viewModel.getTrainList(currentStation).observe(viewLifecycleOwner, trainListObserver)
        }
    }

    private fun handleGetTrainListSuccess(trainList: List<TrainUiModel>) {
        swipe_container.isRefreshing = false
        rv_station_list?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = TrainListAdapter(trainList)
        }
    }

    private fun handleGetTrainListStatusError() {
        progress_bar.visibility = View.GONE
        swipe_container.isRefreshing = false
        Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance() =
            TrainListFragment()
    }
}
