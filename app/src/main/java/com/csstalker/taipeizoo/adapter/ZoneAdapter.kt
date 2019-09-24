package com.csstalker.taipeizoo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.csstalker.taipeizoo.R
import com.csstalker.taipeizoo.gson.`object`.ZoneData
import com.csstalker.taipeizoo.image.GlideConfig
import com.csstalker.zoo.adapter.OnZoneItemClickListener

class ZoneAdapter(private val context: Context, private val zoneList: List<ZoneData>?) : RecyclerView.Adapter<ZoneAdapter.ZoneViewHolder>() {
    private var zicListener: OnZoneItemClickListener? = null

    fun setOnZoneItemClickListener(zicListener: OnZoneItemClickListener) {
        this.zicListener = zicListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ZoneViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.zone_listitem, viewGroup, false)
        return ZoneViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ZoneViewHolder, position: Int) {
        val zone = zoneList!![position]
        // 圖片
        val imageUrl = zone.img
        if (imageUrl != null && "" != imageUrl) {
            Glide.with(context)
                .load(imageUrl)
                .apply(GlideConfig.getInstance().glideOption)
                .into(holder.zoneImage)
        }
        // 標題
        holder.titleText.text = zone.name
        // 介紹
        holder.descText.text = zone.info
        // 休館資訊
        var memo: String? = zone.memo
        if (memo == null || "" == memo)
            memo = context.getString(R.string.empty_open_hour_text)
        holder.openHourText.text = memo
        // set click listener
        holder.itemLayout.setOnClickListener {
            if (zicListener != null)
                zicListener!!.onClickZone(zone)
        }
    }

    override fun getItemCount(): Int {
        return zoneList?.size ?: 0
    }

    inner class ZoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var itemLayout: RelativeLayout
        internal var zoneImage: ImageView
        internal var titleText: TextView
        internal var descText: TextView
        internal var openHourText: TextView

        init {
            itemLayout = itemView.findViewById(R.id.zone_item_layout)
            zoneImage = itemView.findViewById(R.id.zone_image)
            titleText = itemView.findViewById(R.id.zone_title_text)
            descText = itemView.findViewById(R.id.zone_desc_text)
            openHourText = itemView.findViewById(R.id.zone_open_hour_desc_text)
        }
    }

}