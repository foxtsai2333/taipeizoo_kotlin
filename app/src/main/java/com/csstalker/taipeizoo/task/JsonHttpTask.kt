package com.csstalker.taipeizoo.task

import android.os.AsyncTask
import android.util.Log
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader.TIMEOUT
import com.csstalker.taipeizoo.gson.GsonTool
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import javax.security.auth.login.LoginException

class JsonHttpTask : AsyncTask<String, String, Any> {

    // static part
    companion object {
        private val TAG = JsonHttpTask::class.simpleName
        private val TIMEOUT = 60
    }

    private var client: OkHttpClient? = null
    private var jtcListener: OnJsonTaskCompleteListener? = null
    private var c: Class<*>? = null
    private var body: RequestBody? = null
    private var url: String? = null


    constructor(url: String, c: Class<*>, jtcListener: OnJsonTaskCompleteListener) {
        // for get
        this.c = c
        this.url = url
        this.body = null
        this.jtcListener = jtcListener
        client = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .build()
    }

    constructor(url: String, body: RequestBody, c: Class<*>, jtcListener: OnJsonTaskCompleteListener) {
        // for post
        this.c = c
        this.url = url
        this.body = body
        this.jtcListener = jtcListener
        client = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .build()
    }

    override fun doInBackground(vararg params: String?): Any? {
        try {
            val request = getRequest(url, body)
            val response = client!!.newCall(request).execute()
            val result = String(response.body!!.bytes(), StandardCharsets.UTF_8)
            return GsonTool.getInstance().stringToObject(result, c!!)
        } catch (e: Exception) {
            Log.e(TAG, e.message)
            return null
        }
    }

    override fun onPostExecute(`object`: Any) {
        if (jtcListener != null)
            jtcListener!!.onTaskComplete(`object`)
    }

    private fun getRequest(url: String?, body: RequestBody?): Request {
        return if (body == null) {
            // get
            Request.Builder()
                .url(url!!)
                .build()
        } else {
            // post
            Request.Builder()
                .url(url!!)
                .post(body)
                .build()
        }
    }


}