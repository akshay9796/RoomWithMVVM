package com.example.extraaedge.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONArray

@Entity(tableName = "RocketData")
data class RocketDataList(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "type")
    var type: String = "",

    @ColumnInfo(name = "active")
    var active: Boolean = false,

    @ColumnInfo(name = "stages")
    var stages: Int = 0,

    @ColumnInfo(name = "boosters")
    var boosters: Int = 0,

    @ColumnInfo(name = "cost_per_launch")
    var cost_per_launch: Int = 0,

    @ColumnInfo(name = "success_rate_pct")
    var success_rate_pct: Int = 0,

    @ColumnInfo(name = "first_flight")
    var first_flight: String = "",

    @ColumnInfo(name = "country")
    var country: String = "",

    @ColumnInfo(name = "company")
    var company: String = "",

    @ColumnInfo(name = "wikipedia")
    var wikipedia: String = "",

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "flickr_images")
    var imageList: String? = "",

    @ColumnInfo(name = "h_meters")
    var hMeters: Double = 0.0,

    @ColumnInfo(name = "h_feet")
    var hFeet: Double = 0.0,

    @ColumnInfo(name = "d_meters")
    var dMeters: Double = 0.0,

    @ColumnInfo(name = "d_feet")
    var dFeet: Double = 0.0

) {

}

