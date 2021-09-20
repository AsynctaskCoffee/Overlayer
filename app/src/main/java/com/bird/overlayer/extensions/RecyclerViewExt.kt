package com.bird.overlayer.extensions

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Sets an empty state view for the Recycler view which automatically shown/hidden based on
 * current state of the data
 */
fun <VH : RecyclerView.ViewHolder> RecyclerView.Adapter<VH>.setEmptyStateView(view: View): RecyclerView.AdapterDataObserver {
    val dataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() = checkForEmptyState()
        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = checkForEmptyState()
        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) = checkForEmptyState()
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = checkForEmptyState()
        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) = checkForEmptyState()
        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) = checkForEmptyState()

        fun checkForEmptyState() {
            view.visible = itemCount == 0
        }
    }

    // registers data set observers that checks and manages the empty state view
    registerAdapterDataObserver(dataObserver)

    // also make an initial call to checkForEmptyState
    dataObserver.checkForEmptyState()

    return dataObserver
}