package com.csstalker.taipeizoo.app

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.csstalker.taipeizoo.R
import com.csstalker.taipeizoo.adapter.ZoneAdapter
import com.csstalker.taipeizoo.api.APITool
import com.csstalker.taipeizoo.gson.GsonTool
import com.csstalker.taipeizoo.gson.`object`.ZoneBase
import com.csstalker.taipeizoo.gson.`object`.ZoneData
import com.csstalker.taipeizoo.task.JsonHttpTask
import com.csstalker.taipeizoo.task.OnJsonTaskCompleteListener
import com.csstalker.zoo.adapter.OnZoneItemClickListener
import java.lang.Exception

class HomeActivity : BaseActivity(),
    OnZoneItemClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private val TAG = HomeActivity::class.simpleName
    }

    // view
    private var toolbar: Toolbar?= null
    private var drawerLayout: DrawerLayout?= null
    private var navigationView: NavigationView? = null
    private var zoneRecyclerView: RecyclerView? = null
    private var emptyText: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findviews()
        setlistener()
        init()
    }

    fun findviews() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = this.findViewById(R.id.nav_view)
        zoneRecyclerView = findViewById(R.id.zone_recyclerview)
        emptyText = findViewById(R.id.empty_text)
    }

    fun setlistener() {
        // 讓 toolbar 上的標題按鈕跟抽屜開關同步作動
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()

        // 設定選單項目點選的 listener
        navigationView!!.setNavigationItemSelectedListener(this)
    }

    fun init() {
        // 拿動物園資料
        val task = JsonHttpTask(APITool.zoneUrl, ZoneBase::class.java, object : OnJsonTaskCompleteListener {
            override fun onTaskComplete(obj: Any) {
                Log.d(TAG, "onTaskComplete")
                dismissLoadingHint()
                val zb = obj as ZoneBase
                try {
                    if (zb != null && zb.result!!.zoneList!!.size > 0) {
                        emptyText!!.visibility = View.GONE
                        val adapter = ZoneAdapter(this@HomeActivity, zb.result!!.zoneList)
                        zoneRecyclerView!!.layoutManager = LinearLayoutManager(this@HomeActivity)
                        zoneRecyclerView!!.setHasFixedSize(true)
                        zoneRecyclerView!!.adapter = adapter
                        adapter.setOnZoneItemClickListener(this@HomeActivity)
                    } else {
                        // 顯示空白提示文字
                        emptyText!!.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "onTaskComplete: " + e.message)
                }
            }
        })
        showLoadingHint()
        task.execute()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_version_info -> {
                // 顯示版本資訊
                Toast.makeText(this, versionName, Toast.LENGTH_LONG).show()
                return true
            }
        }
        return false
    }

    override fun onClickZone(zone: ZoneData) {
        Log.d(TAG, "onClickZone: " + zone.name!!)
        val intent = Intent(this, ZoneActivity::class.java)
        intent.action = Intent.ACTION_VIEW
        intent.putExtra("zone", GsonTool.getInstance().objectToString(zone))
        startActivity(intent)

    }
}
