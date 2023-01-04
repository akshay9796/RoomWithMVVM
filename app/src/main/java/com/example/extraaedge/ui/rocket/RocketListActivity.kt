package com.example.extraaedge.ui.rocket

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.extraaedge.R
import com.example.extraaedge.di.utils.Status
import com.example.extraaedge.di.utils.ViewModelFactory
import com.example.extraaedge.domain.model.RocketDataList
import com.example.extraaedge.network.ApiResponse
import com.example.extraaedge.room.AppDatabase
import com.example.extraaedge.ui.Navigator
import com.example.extraaedge.ui.adapter.RocketAdapter
import com.example.extraaedge.ui.base.BaseActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import javax.inject.Inject


class RocketListActivity : BaseActivity<RocketViewModel?>() {

    var viewModelFactory: ViewModelFactory<RocketViewModel>? = null
        @Inject
        set

    override var viewModel: RocketViewModel? = null
        private set

    var navigator: Navigator? = null
        @Inject
        set

    private var returnToWorkAdapter: RocketAdapter? = null
    var rocketList = ArrayList<RocketDataList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RocketViewModel::class.java)

        val dataBaseInstance = AppDatabase.getDatabasenIstance(this)
        viewModel?.setInstanceOfDb(dataBaseInstance)

        if (!isOnline) {
            Toast.makeText(applicationContext, "Please check your internet connection...", Toast.LENGTH_SHORT).show()
            viewModel?.getRocketDataList()
        } else
            viewModel?.getRocketList()

        setUpUI()

        bindViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUpUI() {
        recycler_rct_list?.setHasFixedSize(false)
        val mLinearLayoutManager = LinearLayoutManager(this)
        recycler_rct_list?.layoutManager = mLinearLayoutManager
        recycler_rct_list?.itemAnimator = null
        returnToWorkAdapter =  RocketAdapter(this, rocketList)
        recycler_rct_list?.adapter = returnToWorkAdapter
        returnToWorkAdapter?.notifyDataSetChanged()

        returnToWorkAdapter?.setOnItemClickListener(object : RocketAdapter.OnItemClickListener {
            override fun onRocketListItemClick(rocketDataList: RocketDataList) {
                navigator?.navigateToRocketDetailsScreen(this@RocketListActivity,rocketDataList)
            }
        })
    }

    private fun bindViewModel() {

        viewModel!!.getRocketList
            .observe(this, { rocketList: ApiResponse? ->
                rocketList != null
                when (rocketList!!.status) {
                    Status.SUCCESS -> try {
                        Handler().postDelayed({ viewModel?.getRocketDataList() }, 3000)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    Status.ERROR -> try {
                        Handler().postDelayed({ viewModel?.getRocketDataList() }, 3000)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

        viewModel?.getRocketDataList?.observe(this, { rocketList ->
            if (rocketList.size > 0) {
                recycler_rct_list.visibility = View.VISIBLE
                txtEmpty.visibility = View.GONE
                returnToWorkAdapter?.setData(rocketList)
            } else {
                txtEmpty.visibility = View.VISIBLE
                recycler_rct_list.visibility = View.GONE
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}