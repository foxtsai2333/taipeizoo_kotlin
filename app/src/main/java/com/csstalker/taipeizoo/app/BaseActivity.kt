package com.csstalker.taipeizoo.app

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.csstalker.taipeizoo.R

open class BaseActivity : AppCompatActivity() {

    private val TAG = BaseActivity::class.simpleName

    val versionName: String
        get() {
            val context = applicationContext
            val packageManager = context.packageManager
            val packageName = context.packageName
            var versionName = "unknown"
            try {
                versionName = packageManager.getPackageInfo(packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e(TAG, "getVersionName:$versionName")
            }

            return versionName
        }

    private var loadingHint: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val isLoadingHintShowing: Boolean
        get() = loadingHint != null && loadingHint!!.isShowing

    fun showLoadingHint() {
        if (!isLoadingHintShowing)
            loadingHint = ProgressDialog.show(this, "", getString(R.string.loading), true)
    }

    fun dismissLoadingHint() {
        if (isLoadingHintShowing)
            loadingHint!!.dismiss()
    }

}
