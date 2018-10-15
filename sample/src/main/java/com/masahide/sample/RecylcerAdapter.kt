package com.masahide.sample

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.masahide.recyclerview.NotifiableThresholdRatioRecyclerView

class RecyclerAdapter(private val mContext: Context, private val dataList: MutableList<String>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_row, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.textView.text = dataList[i]
    }

    override fun getItemCount(): Int {
        return dataList.count()
    }

    /**
     * please extends NotifiableViewHolder instead ViewHolder
     * this ViewHolder will change background color when display area  becomes smaller or
     * greater than 0.5f
     */
    inner class ViewHolder(itemView: View) : NotifiableThresholdRatioRecyclerView.NotifiableViewHolder(itemView) {

        /**
         * call this method when display are is greater than notifyDisplayThresholdRatio
         */
        override fun onDisplay() {
            itemView.setBackgroundColor(Color.BLUE)
        }

        /**
         * call this method when display are is less than notifyHideThresholdRatio
         */
        override fun onHide() {
            itemView.setBackgroundColor(Color.GRAY)
        }

        var textView: TextView = itemView.findViewById(R.id.itemRow)

    }
}