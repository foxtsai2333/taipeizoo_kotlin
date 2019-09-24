package com.csstalker.taipeizoo.app


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.csstalker.taipeizoo.R
import com.csstalker.taipeizoo.gson.GsonTool
import com.csstalker.taipeizoo.gson.`object`.PlanetData
import com.csstalker.taipeizoo.image.GlideConfig
import com.csstalker.taipeizoo.utils.Utils


class PlanetFragment : Fragment() {

    companion object {

        private val TAG = PlanetFragment::class.java.simpleName

        private val ARG_PLANET = "ARG_PLANET"

        fun newInstance(planetStr: String): PlanetFragment {
            val fragment = PlanetFragment()
            val args = Bundle()
            args.putString(ARG_PLANET, planetStr)
            fragment.arguments = args
            return fragment
        }
    }

    // view
    var planetImage: ImageView? = null
    var planetChineseNameText: TextView? = null
    var planetEnglishNameText: TextView? = null
    var planetAliasNameText: TextView? = null
    var planetDescText: TextView? = null
    var planetIdentifyText: TextView? = null
    var planetFunctionalText: TextView? = null
    var updateDateText: TextView? = null

    // misc
    var planetStr: String? = null
    var planet: PlanetData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            planetStr = arguments!!.getString(ARG_PLANET)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        // inflate view
        val itemView = inflater.inflate(R.layout.fragment_planet, container, false)
        planetImage = itemView.findViewById(R.id.planet_image)
        planetChineseNameText = itemView.findViewById(R.id.planet_chinese_name_text)
        planetEnglishNameText = itemView.findViewById(R.id.planet_english_name_text)
        planetAliasNameText = itemView.findViewById(R.id.planet_alias_name_text)
        planetDescText = itemView.findViewById(R.id.planet_desc_text)
        planetIdentifyText = itemView.findViewById(R.id.planet_identify_text)
        planetFunctionalText = itemView.findViewById(R.id.planet_functional_text)
        updateDateText = itemView.findViewById(R.id.update_date_text)

        setPlanetData()

        return itemView
    }

    private fun setPlanetData() {
        if (planetStr == null || "" == planetStr)
            Toast.makeText(context, getString(R.string.empty_planet_data), Toast.LENGTH_LONG).show()
        else {
            planet = GsonTool.getInstance()
                .stringToObject(planetStr, PlanetData::class.java) as PlanetData

            // set data
            if (planet != null) {
                // 顯示標題
                updateTitle(Utils.getInstance().checkDisplayText(planet!!.chineseName))
                // 圖片
                val imageUrl = planet!!.img
                if (imageUrl != null && "" != imageUrl) {
                    Glide.with(this)
                        .load(imageUrl)
                        .apply(GlideConfig.getInstance().glideOptionForPlanet)
                        .into(planetImage!!)
                }
                // 中文名
                planetChineseNameText!!.text = Utils.getInstance().checkDisplayText(planet!!.chineseName)
                // 英文名
                planetEnglishNameText!!.text = Utils.getInstance().checkDisplayText(planet!!.englishName)
                // 別名
                planetAliasNameText!!.text = Utils.getInstance().checkDisplayText(planet!!.alsoKnown)
                // 簡介
                planetDescText!!.text = Utils.getInstance().checkDisplayText(planet!!.brief)
                // 辨認方式
                planetIdentifyText!!.text = Utils.getInstance().checkDisplayText(planet!!.feature)
                // 功能性
                planetFunctionalText!!.text = Utils.getInstance().checkDisplayText(planet!!.funtional)
                // 更新日期
                updateDateText!!.text = Utils.getInstance().checkDisplayText(planet!!.updateDate)
            }
        }
    }

    // 設定標題
    private fun updateTitle(title: String) {
        try {
            activity!!.title = title
        } catch (e: Exception) {
            Log.e(TAG, "updateTitle: " + e.message)
        }

    }


}
