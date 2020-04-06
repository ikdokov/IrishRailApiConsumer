package com.ikdokov.irishrailconsumer.ui.train

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ikdokov.irishrailconsumer.R
import com.ikdokov.irishrailconsumer.ui.model.TrainUiModel
import kotlinx.android.synthetic.main.row_item_train.view.*

class TrainListAdapter(private val trainList: List<TrainUiModel>) :
    RecyclerView.Adapter<TrainListAdapter.TrainListViewHolder>() {

    class TrainListViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrainListViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_train, parent, false)
        return TrainListViewHolder(
            layout
        )
    }

    override fun onBindViewHolder(holder: TrainListViewHolder, position: Int) {
        holder.view.apply {
            tv_arrival_time.text = trainList[position].expArrival
            tv_train_code.text = trainList[position].trainCode
            tv_train_destination.text = context?.resources?.getString(
                R.string.to_destination,
                trainList[position].destination
            )
            holder.view.tv_time_left.text = context?.resources?.getString(
                R.string.minutes_left,
                trainList[position].arivalTimeLeft.toString()
            )
        }
    }

    override fun getItemCount() = trainList.size
}