package com.bird.overlayer.ui.components.main

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bird.overlayer.R
import com.bird.overlayer.data.local.models.Item
import com.bird.overlayer.extensions.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_layout.view.*


class ItemsAdapter(private val itemClicked: (Item) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var selectedPosition = 0
    var items = arrayListOf(Item(-1, "None", "", "", null, null))
    val listTxt = arrayListOf<SelectionComponent>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = parent.inflate(R.layout.item_layout)
        return ViewHolder(itemView)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position], position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Item, position: Int) = with(itemView) {

            if (!listTxt.contains(SelectionComponent(titleTv, titleCard)))
                listTxt.add(SelectionComponent(titleTv, titleCard))

            if (selectedPosition == position) {
                for (i in listTxt) {
                    i.cardView.setCardBackgroundColor(Color.parseColor("#333333"))
                    i.textView.setTextColor(Color.WHITE)
                }
                titleCard.setCardBackgroundColor(resources.getColor(R.color.colorAccent))
                titleTv.setTextColor(resources.getColor(R.color.colorAccent))
            } else {
                titleCard.setCardBackgroundColor(Color.parseColor("#333333"))
                titleTv.setTextColor(Color.WHITE)
            }

            titleTv.text = item.overlayName
            if (item.dataThumb != null) {
                overlayImageView.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        item.dataThumb,
                        0,
                        item.dataThumb!!.size
                    )
                )
            } else {
                overlayImageView.setImageResource(R.drawable.ic_none)
            }
            itemView.setOnClickListener {
                selectedPosition = position
                for (i in listTxt) {
                    i.cardView.setCardBackgroundColor(Color.parseColor("#333333"))
                    i.textView.setTextColor(Color.WHITE)
                }
                titleCard.setCardBackgroundColor(resources.getColor(R.color.colorAccent))
                titleTv.setTextColor(resources.getColor(R.color.colorAccent))
                itemClicked(item)
            }
        }
    }

    inner class SelectionComponent(val textView: TextView, val cardView: CardView)
}