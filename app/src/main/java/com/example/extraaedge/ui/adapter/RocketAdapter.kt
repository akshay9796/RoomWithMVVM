package com.example.extraaedge.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.extraaedge.R
import com.example.extraaedge.domain.model.RocketDataList
import org.json.JSONArray

class RocketAdapter(val context: Context, var rocketList: ArrayList<RocketDataList>)
    : RecyclerView.Adapter<RocketAdapter.ViewHolder>() {

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.item_list_rocket, parent, false))

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        holder.txtName.setText("Name : "+rocketList.get(position).name)
        holder.txtCountry.setText("Country : "+ rocketList.get(position).country)
        holder.txtCount.setText("Count : "+rocketList.get(position).success_rate_pct)
        holder.recycler_img_list.setHasFixedSize(false)
        val mLinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        holder.recycler_img_list.layoutManager = mLinearLayoutManager
        holder.recycler_img_list.itemAnimator = null
        val rocketImgAdapter =  RocketImgAdapter(context, JSONArray(rocketList.get(position).imageList))
        holder.recycler_img_list.adapter = rocketImgAdapter
        rocketImgAdapter.notifyDataSetChanged()

        rocketImgAdapter.setOnItemClickListener(object : RocketImgAdapter.OnItemClickListener {
            override fun onRocketListItemClick(result: String) {
                listener?.onRocketListItemClick(rocketList[position])
            }
        })

        holder.itemView.setOnClickListener {
            listener?.onRocketListItemClick(rocketList[position])
        }

        holder.itemView.setOnClickListener {
            listener?.onRocketListItemClick(rocketList[position])
        }
    }

    override fun getItemCount(): Int {
        return rocketList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txtName)
        val txtCountry: TextView = view.findViewById(R.id.txtCountry)
        val txtCount: TextView = view.findViewById(R.id.txtCount)
        val recycler_img_list: RecyclerView = view.findViewById(R.id.recycler_img_list)

    }

    fun setData(newData: List<RocketDataList>) {
        rocketList.clear()
        this.rocketList.addAll(newData)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onRocketListItemClick(result: RocketDataList)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }
}