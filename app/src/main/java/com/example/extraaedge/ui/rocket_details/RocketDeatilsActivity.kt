package com.example.extraaedge.ui.rocket_details

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.extraaedge.R
import com.example.extraaedge.di.utils.Status
import com.example.extraaedge.di.utils.ViewModelFactory
import com.example.extraaedge.network.ApiResponse
import com.example.extraaedge.room.AppDatabase
import com.example.extraaedge.ui.Navigator
import com.example.extraaedge.ui.adapter.RocketImgAdapter
import com.example.extraaedge.ui.base.BaseActivity
import com.example.extraaedge.ui.rocket.RocketViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_rocket_deatils.*
import org.json.JSONArray
import javax.inject.Inject

class RocketDeatilsActivity : BaseActivity<RocketViewModel?>() {

    var viewModelFactory: ViewModelFactory<RocketViewModel>? = null
        @Inject
        set

    override var viewModel: RocketViewModel? = null
        private set

    var navigator: Navigator? = null
        @Inject
        set

    private var rocketImgAdapter: RocketImgAdapter? = null
    var rocketList = JSONArray()
    private var id: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_rocket_deatils)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RocketViewModel::class.java)

        val dataBaseInstance = AppDatabase.getDatabasenIstance(this)
        viewModel?.setInstanceOfDb(dataBaseInstance)

        if (intent.hasExtra("ROCKET_ID") && !intent.getStringExtra("ROCKET_ID").isNullOrEmpty()) {
            id = intent.getStringExtra("ROCKET_ID")
        }

        if (!isOnline) {
            Toast.makeText(
                applicationContext,
                "Please check your internet connection...",
                Toast.LENGTH_SHORT
            ).show()
            viewModel?.getRocketData(id)
        } else {
            viewModel?.getRocketDetails(id)
        }

        setUpUI()

        bindViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUpUI() {
        recycler_img_list?.setHasFixedSize(false)
        val mLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_img_list?.layoutManager = mLinearLayoutManager
        recycler_img_list?.itemAnimator = null
        rocketImgAdapter = RocketImgAdapter(this, rocketList)
        recycler_img_list?.adapter = rocketImgAdapter
        rocketImgAdapter?.notifyDataSetChanged()

        rocketImgAdapter?.setOnItemClickListener(object : RocketImgAdapter.OnItemClickListener {
            override fun onRocketListItemClick(result: String) {

            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun bindViewModel() {

        viewModel!!.getRocketDetails
            .observe(this, { rocketList: ApiResponse? ->
                rocketList != null
                when (rocketList!!.status) {
                    Status.SUCCESS -> try {
                        viewModel?.getRocketData(id)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    Status.ERROR -> try {
                        viewModel?.getRocketData(id)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

        viewModel?.getRocketData?.observe(this, { rocketData ->
            if (rocketData != null) {
                txtName.setText("Name : " + rocketData.name)
                txtStatus.setText("Active Status : " + rocketData.active)
                txtCost.setText("Cost per launch : " + rocketData.cost_per_launch)
                txtSuccessRate.setText("Success Rate percent : " + rocketData.success_rate_pct)
                txtDescreption.setText("Description : " + rocketData.description)
                setupHyperlink(rocketData.wikipedia)
                txtHight.setText("Height & Diameter in Feet/Inches : " + rocketData.hMeters +"/" +rocketData.hFeet+" & "+ rocketData.dMeters +"/" +rocketData.dFeet)
                rocketImgAdapter?.setData(JSONArray(rocketData.imageList))
            }
        })
    }

    fun setupHyperlink(wikipedia: String) {
        val mSpannableString = SpannableString(wikipedia)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        txtLink.text = mSpannableString
        txtLink.setTextColor(Color.BLUE)
        txtLink.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(wikipedia))
            startActivity(browserIntent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}