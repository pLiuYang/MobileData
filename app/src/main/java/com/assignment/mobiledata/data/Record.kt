package com.assignment.mobiledata.data

import org.json.JSONObject

data class Record(
    val volumeOfMobileData: String,
    val quarter: String,
    val id: Int
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
