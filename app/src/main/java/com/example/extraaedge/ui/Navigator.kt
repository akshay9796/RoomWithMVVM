package com.example.extraaedge.ui

import android.app.Activity
import android.content.Intent
import com.example.extraaedge.domain.model.RocketDataList
import com.example.extraaedge.ui.rocket_details.RocketDeatilsActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor(){

    fun navigateToRocketDetailsScreen(context: Activity, rocketDataList: RocketDataList?) {
        val intent: Intent
        intent = Intent(context, RocketDeatilsActivity::class.java)
        intent.putExtra("ROCKET_ID", rocketDataList?.id)
        context.startActivity(intent)
    }
}