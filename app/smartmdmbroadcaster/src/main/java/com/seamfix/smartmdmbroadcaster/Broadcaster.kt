package com.seamfix.smartmdmbroadcaster

import android.app.admin.DevicePolicyManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.Keep

const val MDM_ACTION = "com.seamfix.DEVICE_IDENTIFIERS"

@Keep
open class Broadcaster: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent != null && intent.action == MDM_ACTION) {
            val imei = intent.getStringExtra("IMEI") // this is the device’s default IMEI.
            val admin = intent.getBooleanExtra("RELINQUISH_DEVICE_ADMIN", false)
            val imei_1 = intent.getStringExtra("IMEI_1")
            val imei_2 = intent.getStringExtra("IMEI_2")
            val buildNumber = intent.getStringExtra("BUILD_NUMBER")

            //relinquish device admin rights:
            if(admin && context != null){
                deactivate(context)
            }

            // Data you need to pass to activity
            val i = Intent(MDM_ACTION)
            i.putExtra("IMEI", imei)
            i.putExtra("RELINQUISH_DEVICE_ADMIN", admin)
            i.putExtra("IMEI_1", imei_1)
            i.putExtra("IMEI_2", imei_2)
            i.putExtra("BUILD_NUMBER", buildNumber)
            context!!.sendBroadcast(i)
        }
    }
}


fun deactivate(context: Context)= try{
    val dpm: DevicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    dpm.clearDeviceOwnerApp(context.packageName)
}catch (e: Exception){
}
