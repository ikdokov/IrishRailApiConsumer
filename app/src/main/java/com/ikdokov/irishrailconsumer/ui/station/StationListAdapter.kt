package com.ikdokov.irishrailconsumer.ui.station

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ikdokov.irishrailconsumer.R
import com.ikdokov.irishrailconsumer.ui.model.StationUiModel
import kotlinx.android.synthetic.main.row_item_station.view.*
import kotlin.collections.ArrayList

class StationListAdapter(private val searchableStations: MutableList<StationUiModel>) :
    RecyclerView.Adapter<StationListAdapter.MyViewHolder>(), Filterable {

    private var originalStations = ArrayList(searchableStations)

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                navigateToTrainsScreen(view, view.tv_station_name.text.toString())
            }
        }

        private fun navigateToTrainsScreen(view: View, stationName: String) {
            val bundle = bundleOf("station" to stationName)
            view.findNavController()
                .navigate(R.id.action_stationSearchFragment_to_mainFragment, bundle)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_station, parent, false)
        return MyViewHolder(
            layout
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.view.tv_station_name.text = searchableStations[position].name
    }

    override fun getItemCount() = searchableStations.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                searchableStations.clear()

                return FilterResults().apply {
                    values = if (constraint.isNullOrEmpty()) {
                        searchableStations.addAll(originalStations)
                    } else {
                        searchableStations.addAll(originalStations.filter {
                            it.name.contains(constraint, ignoreCase = true)
                        })
                    }
                    count = searchableStations.size
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }
        }
    }
}