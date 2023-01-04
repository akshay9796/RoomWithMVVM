package com.example.extraaedge.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.extraaedge.domain.model.RocketDataList
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface RocketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateRocket(rtw: RocketDataList?):  Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(task: List<RocketDataList?>?) : Completable

    @Query("SELECT * FROM RocketData ORDER BY name ASC")
    fun getRocketData() : Single<List<RocketDataList>>

    @Query("SELECT * FROM RocketData WHERE id = :id")
    fun getRocketData(id: String?): Single<RocketDataList>

    @Query("DELETE FROM RocketData")
    fun deleteRocket(): Int
}