package com.example.extraaedge.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.extraaedge.R
import com.example.extraaedge.domain.model.RocketDataList
import com.squareup.picasso.Picasso
import org.json.JSONArray

class RocketImgAdapter(val context: Context, var rocketList: JSONArray) :
    RecyclerView.Adapter<RocketImgAdapter.ViewHolder>() {

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_list_rocket_img, parent, false
        )
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get()
            .load(rocketList.get(position).toString())
            .resize(100, 100)
            .centerCrop()
            .into(holder.ivFilcker)

        holder.itemView.setOnClickListener {
            listener?.onRocketListItemClick(rocketList.get(position).toString())
        }
    }

    override fun getItemCount(): Int {
        return rocketList.length()
    }

    fun setData(newData: JSONArray) {
        rocketList = JSONArray()
        this.rocketList = newData
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivFilcker: ImageView = view.findViewById(R.id.ivFilcker)

    }

    interface OnItemClickListener {
        fun onRocketListItemClick(result: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }
}