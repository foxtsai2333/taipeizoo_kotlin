package com.csstalker.taipeizoo.app


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.csstalker.taipeizoo.R
import com.csstalker.taipeizoo.adapter.PlanetAdapter
import com.csstalker.taipeizoo.gson.GsonTool
import com.csstalker.taipeizoo.gson.`object`.PlanetData
import com.csstalker.taipeizoo.gson.`object`.PlanetResult
import com.csstalker.taipeizoo.gson.`object`.ZoneData
import com.csstalker.taipeizoo.image.GlideConfig
import com.csstalker.taipeizoo.utils.Utils
import com.csstalker.zoo.adapter.OnPlanetItemClickListener

class ZoneFragment : Fragment(), OnPlanetItemClickListener {

    companion object {

        private val TAG = ZoneFragment::class.java.simpleName

        private val ARG_ZONE = "ARG_ZONE"
        private val ARG_PLANET = "ARG_PLANET"

        // 用來在 fragment 生成時帶入所需資料
        fun newInstance(zoneStr: String, planetStr: String): ZoneFragment {
            val fragment = ZoneFragment()
            val args = Bundle()
            args.putString(ARG_ZONE, zoneStr)
            args.putString(ARG_PLANET, planetStr)
            fragment.arguments = args
            return fragment
        }
    }

    // view
    var zoneImage: ImageView? = null
    var descText: TextView? = null
    var openHourText: TextView? = null
    var zoneTypeText: TextView? = null
    var webText: TextView? = null
    var planetRecyclerView: RecyclerView? = null
    var planetEmptyText: TextView? = null

    var picListener: OnPlanetItemClickListener? = null

    // misc
    var zone: ZoneData? = null
    var planetResult: PlanetResult? = null

    fun setOnPlanetItemClickListener(picListener: OnPlanetItemClickListener) {
        this.picListener = picListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val zoneStr = arguments!!.getString(ARG_ZONE)
            val planetStr = arguments!!.getString(ARG_PLANET)
            zone = GsonTool.getInstance().stringToObject(zoneStr, ZoneData::class.java) as ZoneData
            planetResult = GsonTool.getInstance().stringToObject(planetStr, PlanetResult::class.java) as PlanetResult
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        // inflate layout
        val itemView = inflater.inflate(R.layout.fragment_zone, container, false)
        // findview
        zoneImage = itemView.findViewById(R.id.zone_image)
        descText = itemView.findViewById(R.id.zone_desc_text)
        openHourText = itemView.findViewById(R.id.zone_open_hour_desc_text)
        zoneTypeText = itemView.findViewById(R.id.zone_type_text)
        webText = itemView.findViewById(R.id.zone_web_text)
        planetRecyclerView = itemView.findViewById(R.id.planet_recyclerview)
        planetEmptyText = itemView.findViewById(R.id.zone_planet_empty_text)

        setZoneData()
        setPlanetData()

        return itemView
    }

    fun setZoneData() {
        if (zone == null) {
            Toast.makeText(context, "something wrong...", Toast.LENGTH_LONG).show()
        } else {
            // 設定標題
            updateTitle(Utils.getInstance().checkDisplayText(zone!!.name))
            // 設定該館的資訊
            // 圖片
            val imageUrl = zone!!.img
            if (imageUrl != null && "" != imageUrl) {
                Glide.with(this)
                    .load(imageUrl)
                    .apply(GlideConfig.getInstance().glideOption)
                    .into(zoneImage!!)
            }
            // 說明
            descText!!.text = zone!!.info
            // 休館資訊
            var memo: String? = zone!!.memo
            if (memo == null || "" == memo)
                memo = getString(R.string.empty_open_hour_text)
            openHourText!!.text = memo
            // 分區
            zoneTypeText!!.text = Utils.getInstance().checkDisplayText(zone!!.category)
        }
    }

    fun setPlanetData() {
        // 設定 adapter
        if (planetResult == null
            || planetResult!!.planetList == null
            || planetResult!!.planetList.size == 0) {
            planetEmptyText!!.visibility = View.VISIBLE
        } else {
            planetEmptyText!!.visibility = View.GONE
            val adapter = PlanetAdapter(context!!, planetResult!!.planetList)
            planetRecyclerView!!.layoutManager = LinearLayoutManager(context)
            planetRecyclerView!!.setHasFixedSize(true)
            planetRecyclerView!!.isNestedScrollingEnabled = false
            planetRecyclerView!!.adapter = adapter
            adapter.setOnClickPlanetItemListener(this)
        }
    }

    override fun onClickPlanet(planet: PlanetData) {
        if (picListener != null)
            picListener!!.onClickPlanet(planet)
    }

    // 設定標題
    fun updateTitle(title: String) {
        try {
            activity!!.title = title
        } catch (e: Exception) {
            Log.e(TAG, "updateTitle: " + e.message)
        }

    }


}
