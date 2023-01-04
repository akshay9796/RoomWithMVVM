package com.example.extraaedge.ui.rocket

import androidx.lifecycle.MutableLiveData
import com.example.extraaedge.domain.model.RocketDataList
import com.example.extraaedge.domain.usecase.RocketUseCase
import com.example.extraaedge.network.ApiResponse
import com.example.extraaedge.room.AppDatabase
import com.example.extraaedge.ui.base.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import io.reactivex.android.schedulers.AndroidSchedulers
import org.json.JSONArray

class RocketViewModel @Inject internal constructor(rocketUseCase: RocketUseCase) : BaseViewModel() {

    private val disposables = CompositeDisposable()
    private val rocketUseCase: RocketUseCase
    private val apiResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    private val apiRocketDeatilsResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    private val getList: MutableLiveData<List<RocketDataList>> = MutableLiveData()
    private val getRocket: MutableLiveData<RocketDataList> = MutableLiveData()
    private var dataBaseInstance: AppDatabase? = null

    fun setInstanceOfDb(dataBaseInstance: AppDatabase) {
        this.dataBaseInstance = dataBaseInstance
    }

    fun getRocketList() {
        showLoading(true)
        rocketUseCase.getRocketList()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe({ data -> apiResponse.setValue(ApiResponse.loading()) })
            ?.subscribe({ data ->
//                showLoading(false)
                val responce: String = java.lang.String.valueOf(data)
                val ocketArray = JSONArray(responce)
                val rocketDataList: ArrayList<RocketDataList> = ArrayList()
                for (i in 0 until ocketArray.length()) {
                    val jsonObject = ocketArray.getJSONObject(i)
                    val rocketData = RocketDataList(
                        jsonObject.getString("id"),
                        jsonObject.getString("name"),
                        jsonObject.getString("type"),
                        jsonObject.getBoolean("active"),
                        jsonObject.getInt("stages"),
                        jsonObject.getInt("boosters"),
                        jsonObject.getInt("cost_per_launch"),
                        jsonObject.getInt("success_rate_pct"),
                        jsonObject.getString("first_flight"),
                        jsonObject.getString("country"),
                        jsonObject.getString("company"),
                        jsonObject.getString("wikipedia"),
                        jsonObject.getString("description"),
                        jsonObject.getJSONArray("flickr_images").toString(),
                        jsonObject.getJSONObject("height").getDouble("meters"),
                        jsonObject.getJSONObject("height").getDouble("feet"),
                        jsonObject.getJSONObject("diameter").getDouble("meters"),
                        jsonObject.getJSONObject("diameter").getDouble("feet")
                    )
                    rocketDataList.add(rocketData)
                }
                insertData(rocketDataList)
                apiResponse.setValue(data?.let { ApiResponse.success(it) })
            }, { error ->
                showLoading(false)
                apiResponse.setValue(ApiResponse.error(error))
            })?.let {
                disposables.add(
                    it
                )
            }
    }

    fun getRocketDetails(id: String?) {
        showLoading(true)
        rocketUseCase.getRocketDetails(id)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe({ data -> apiRocketDeatilsResponse.setValue(ApiResponse.loading()) })
            ?.subscribe({ data ->
//                showLoading(false)
                val responce: String = java.lang.String.valueOf(data)
                val ocketArray = JSONArray(responce)
                for (i in 0 until ocketArray.length()) {
                    val jsonObject = ocketArray.getJSONObject(i)
                    if (jsonObject.getString("id").equals(id)) {
                        val rocketData = RocketDataList(
                            jsonObject.getString("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("type"),
                            jsonObject.getBoolean("active"),
                            jsonObject.getInt("stages"),
                            jsonObject.getInt("boosters"),
                            jsonObject.getInt("cost_per_launch"),
                            jsonObject.getInt("success_rate_pct"),
                            jsonObject.getString("first_flight"),
                            jsonObject.getString("country"),
                            jsonObject.getString("company"),
                            jsonObject.getString("wikipedia"),
                            jsonObject.getString("description"),
                            jsonObject.getJSONArray("flickr_images").toString(),
                            jsonObject.getJSONObject("height").getDouble("meters"),
                            jsonObject.getJSONObject("height").getDouble("feet"),
                            jsonObject.getJSONObject("diameter").getDouble("meters"),
                            jsonObject.getJSONObject("diameter").getDouble("feet")
                        )
                        updateRocketData(rocketData)
                        break
                    }
                }
                apiRocketDeatilsResponse.setValue(data?.let { ApiResponse.success(it) })
            }, { error ->
                showLoading(false)
                apiRocketDeatilsResponse.setValue(ApiResponse.error(error))
            })?.let {
                disposables.add(
                    it
                )
            }
    }

    fun insertData(rocketList: List<RocketDataList>?) {
        dataBaseInstance?.rocketDao()?.insertList(rocketList)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
            }, { error ->
            })?.let {
                disposables.add(it)
            }
    }

    fun getRocketDataList() {
        dataBaseInstance?.rocketDao()?.getRocketData()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                showLoading(false)
                if (!it.isNullOrEmpty()) {
                    getList.postValue(it)
                } else {
                    getList.postValue(ArrayList())
                }
            }, {
            })?.let {
                disposables.add(it)
            }
    }

    fun updateRocketData(rocketData: RocketDataList?) {
        dataBaseInstance?.rocketDao()?.updateRocket(rocketData)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
            }, { error ->
            })?.let {
                disposables.add(it)
            }
    }

    fun getRocketData(id: String?) {
        dataBaseInstance?.rocketDao()?.getRocketData(id)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                showLoading(false)
                getRocket.postValue(it)
            }, {
            })?.let {
                disposables.add(it)
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    val getRocketList: MutableLiveData<ApiResponse>
        get() = apiResponse

    val getRocketDetails: MutableLiveData<ApiResponse>
        get() = apiRocketDeatilsResponse

    val getRocketDataList: MutableLiveData<List<RocketDataList>>
        get() = getList

    val getRocketData: MutableLiveData<RocketDataList>
        get() = getRocket

    init {
        this.rocketUseCase = rocketUseCase
    }
}