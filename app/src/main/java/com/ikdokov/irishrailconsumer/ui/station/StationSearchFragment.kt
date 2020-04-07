package com.ikdokov.irishrailconsumer.ui.station

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikdokov.irishrailconsumer.R
import com.ikdokov.irishrailconsumer.data.model.Resource
import com.ikdokov.irishrailconsumer.ui.model.StationUiModel
import kotlinx.android.synthetic.main.fragment_station_search.*

class StationSearchFragment : Fragment() {

    private lateinit var stationListAdapter: StationListAdapter

    private val viewModel: StationSearchViewModel by lazy {
        ViewModelProvider(this@StationSearchFragment).get(StationSearchViewModel::class.java)
    }

    private val stationListObserver = Observer<Resource<List<StationUiModel>>> { stationListResource ->
        when (stationListResource.status) {
            Resource.STATUS_SUCCESS -> {
                progress_bar.visibility = View.GONE
                stationListResource.data?.let { stations ->
                    handleStationsSuccess(stations)
                }
            }

            Resource.STATUS_ERROR -> {
                handleStationsStatusError()
            }

            Resource.STATUS_LOADING -> {
                progress_bar.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_station_search, menu)
        (menu.findItem(R.id.action_search)?.actionView as SearchView).setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                stationListAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                stationListAdapter.filter.filter(newText)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_station_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)
        setupToolbar()

        viewModel.getAllStations().observe(viewLifecycleOwner, stationListObserver)
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.stations)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
    }

    private fun handleStationsStatusError() {
        progress_bar.visibility = View.GONE
        Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
    }

    private fun handleStationsSuccess(stations: List<StationUiModel>) {
        stationListAdapter = StationListAdapter(stations.toMutableList())
        rv_station_list?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = stationListAdapter
        }
    }

    companion object {
        fun newInstance() =
            StationSearchFragment()
    }
}
