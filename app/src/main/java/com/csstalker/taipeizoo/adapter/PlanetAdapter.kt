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
import com.csstalker.taipeizoo.gson.`object`.PlanetData
import com.csstalker.taipeizoo.image.GlideConfig
import com.csstalker.zoo.adapter.OnPlanetItemClickListener

class PlanetAdapter(private val context: Context, private val planetList: List<PlanetData>?) : RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder>() {
    private var planetItemListener: OnPlanetItemClickListener? = null

    fun setOnClickPlanetItemListener(planetItemListener: OnPlanetItemClickListener) {
        this.planetItemListener = planetItemListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): PlanetViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.planet_listitem, viewGroup, false)
        return PlanetViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlanetViewHolder, position: Int) {
        val planet = planetList!![position]
        // 圖片
        val imageUrl = planet.img
        if (imageUrl != null && "" != imageUrl) {
            Glide.with(context)
                .load(imageUrl)
                .apply(GlideConfig.getInstance().glideOptionForPlanet)
                .into(holder.planetImage)
        }
        // 名稱
        holder.titleText.text = planet.chineseName
        // 說明
        holder.aliasNameText.text = planet.brief

        // 點擊
        holder.itemLayout.setOnClickListener {
            if (planetItemListener != null)
                planetItemListener!!.onClickPlanet(planet)
        }
    }

    override fun getItemCount(): Int {
        return planetList?.size ?: 0
    }

    inner class PlanetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var itemLayout: RelativeLayout
        internal var planetImage: ImageView
        internal var titleText: TextView
        internal var aliasNameText: TextView

        init {
            itemLayout = itemView.findViewById(R.id.planet_item_layout)
            planetImage = itemView.findViewById(R.id.planet_image)
            titleText = itemView.findViewById(R.id.planet_title_text)
            aliasNameText = itemView.findViewById(R.id.planet_alias_name_text)
        }
    }

}