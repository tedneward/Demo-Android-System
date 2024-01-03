package edu.uw.ischool.newart.newlauncher

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


data class PInfo(
    val appname : String = "",
    val pname : String = "",
    val versionName : String = "",
    val versionCode : Long = 0,
    var icon : Drawable? = null
    ) { }

fun getInstalledApps(context : Context, getSysPackages: Boolean): List<PInfo> {
    val res = mutableListOf<PInfo>()
    val packs = context.packageManager.getInstalledPackages(0)
    for (p in packs) {
        if (!getSysPackages && p.versionName == null) {
            continue
        }
        val appname = p.applicationInfo.loadLabel(context.packageManager).toString()
        val pname = p.packageName
        val versionName = p.versionName
        val versionCode = p.longVersionCode
        val icon = p.applicationInfo.loadIcon(context.packageManager)
        res.add(PInfo(appname, pname, versionName, versionCode, icon))
    }
    return res
}

// TODO: Convert this to icon/string/string layout
class PInfoListAdapter(context : Context, apps : List<PInfo>)
    : ArrayAdapter<PInfo>(context, android.R.layout.simple_list_item_1, apps) {

}

class MainActivity : AppCompatActivity() {
    lateinit var adapter : PInfoListAdapter
    lateinit var systemApps : Switch
    lateinit var appList : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        systemApps = findViewById(R.id.swtSystemApps)
        appList = findViewById(R.id.lstApps)

        val apps = getInstalledApps(this, systemApps.isChecked)
        adapter = PInfoListAdapter(this, apps)
        appList.adapter = adapter

        appList.setOnItemLongClickListener { parent, view, position, id ->
            Toast.makeText(this, "Launching ${apps[position].appname}", Toast.LENGTH_SHORT).show()

            val launchIntent = packageManager.getLaunchIntentForPackage(apps[position].pname)
            this.startActivity(launchIntent)

            true
        }
    }
}
