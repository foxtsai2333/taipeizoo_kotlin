package com.csstalker.taipeizoo.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.csstalker.taipeizoo.R
import com.csstalker.taipeizoo.api.APITool
import com.csstalker.taipeizoo.gson.GsonTool
import com.csstalker.taipeizoo.gson.`object`.PlanetBase
import com.csstalker.taipeizoo.gson.`object`.PlanetData
import com.csstalker.taipeizoo.gson.`object`.ZoneData
import com.csstalker.taipeizoo.task.JsonHttpTask
import com.csstalker.taipeizoo.task.OnJsonTaskCompleteListener
import com.csstalker.zoo.adapter.OnPlanetItemClickListener
import java.lang.Exception
import java.net.URLEncoder

class ZoneActivity : BaseActivity(), OnPlanetItemClickListener {

    companion object {
        val TAG = ZoneActivity::class.simpleName
    }

    // view
    var emptyText: TextView? = null

    var fm: FragmentManager? = null

    // misc
    var zone: ZoneData? = null
    var zoneStr = ""
    var planetStr = ""
    var zoneFragment: ZoneFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zone)

        // 開啟返回按鈕
        val bar = supportActionBar
        bar!!.setDisplayHomeAsUpEnabled(true)
        // 取得 fragment manager
        fm = supportFragmentManager

        // get zone object payload
        zoneStr = intent.getStringExtra("zone")
        if ("" != zoneStr) {
            // parsing payload to object
            zone = GsonTool.getInstance()
                .stringToObject(zoneStr, ZoneData::class.java) as ZoneData
        }

        findviews()
        init()
    }

    fun findviews() {
        emptyText = findViewById(R.id.empty_text)
    }

    fun init() {
        getPlanetOfZone(zone!!.name)
    }

    fun getPlanetOfZone(zoneName: String) {
        // 透過 api 查詢館內植物
        var url = APITool.planetUrl
        try {
            url = url + "&q=" + URLEncoder.encode(zoneName, "UTF-8")
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }

        val task = JsonHttpTask(url, PlanetBase::class.java, object : OnJsonTaskCompleteListener {
            override fun onTaskComplete(obj: Any) {
                Log.d(TAG, "onTaskComplete")
                dismissLoadingHint()
                val pb = obj as PlanetBase
                if (pb != null) {
                    Log.d(TAG, "planet list size = " + pb.result.planetList.size)
                    emptyText!!.visibility = View.GONE
                    planetStr = GsonTool.getInstance().objectToString(pb.result)
                    setupFragment()
                } else {
                    // 顯示空白提示
                    emptyText!!.visibility = View.VISIBLE
                    // 設定預設標題
                    title = getString(R.string.app_name)
                }
            }
        })
        showLoadingHint()
        task.execute()
    }

    fun setupFragment() {
        // 用 fragment 切換顯示
        // 建立 fragment transcation
        val ft = fm!!.beginTransaction()
        zoneFragment = ZoneFragment.newInstance(zoneStr, planetStr)
        zoneFragment!!.setOnPlanetItemClickListener(this)
        // 加入 fragment
        ft.add(R.id.container, zoneFragment!!)
        ft.commit()
        // 要求立即處理 pending 的作業
        fm!!.executePendingTransactions()
    }

    override fun onClickPlanet(planet: PlanetData) {
        // 原本會跳頁, 改用 fragment 交換
        val ft = fm!!.beginTransaction()
        val str = GsonTool.getInstance().objectToString(planet)
        val planetFragment = PlanetFragment.newInstance(str!!)
        // 把 zone fragment 換成 planet fragment
        ft.replace(R.id.container, planetFragment, "planet")
            .addToBackStack("zoneToPlanet")
            .commit()
        fm!!.executePendingTransactions()
    }

    fun onClickOpenWeb(view: View) {
        if (zone != null) {
            val url = zone!!.webUrl
            if (url == null) {
                Toast.makeText(this, getString(R.string.web_link_not_found), Toast.LENGTH_LONG).show()
            } else {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // click action bar back
        Log.d(TAG, "onSupportNavigateUp")
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        // click back btn
        Log.d(TAG, "onBackPressed")
        // 先檢查現在是不是顯示植物
        if (fm!!.findFragmentByTag("planet") != null) {
            // 如果有的話, 先跳回 zone
            fm!!.popBackStack("zoneToPlanet", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } else {
            super.onBackPressed()
        }
    }
}
