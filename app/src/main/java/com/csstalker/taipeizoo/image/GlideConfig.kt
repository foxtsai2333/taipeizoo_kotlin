package com.csstalker.taipeizoo.image

import com.bumptech.glide.request.RequestOptions
import com.csstalker.taipeizoo.R

class GlideConfig private constructor() {
    val glideOption: RequestOptions
    val glideOptionForPlanet: RequestOptions

    init {
        glideOption = RequestOptions().placeholder(R.drawable.placeholder_cat)
        glideOptionForPlanet = RequestOptions().placeholder(R.drawable.placeholder_planet)
    }

    companion object {

        private var instance: GlideConfig? = null

        fun getInstance(): GlideConfig {
            if (instance == null)
                instance = GlideConfig()

            return instance as GlideConfig
        }
    }


}
