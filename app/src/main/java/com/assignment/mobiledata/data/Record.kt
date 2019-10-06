package com.assignment.mobiledata.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject

@Entity(tableName = "records")
data class Record(
    @ColumnInfo(name = "volume_of_mobile_data") val volumeOfMobileData: String,
    @ColumnInfo(name = "quarter") val quarter: String,
    @PrimaryKey val id: Int
) {
    constructor(jsonObject: JSONObject) : this(
        jsonObject.optString("volume_of_mobile_data"),
        jsonObject.optString("quarter"),
        jsonObject.optInt("_id")
    )
}

fun Record.getYear() = quarter.substring(0, 4)

fun Record.getVolumeOfDataInFloat(): Float {
    return volumeOfMobileData.toFloatOrNull() ?: 0f
}
