package com.assignment.mobiledata.domain

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Transformations
import com.assignment.mobiledata.data.IMobileDataRepository
import com.assignment.mobiledata.data.Record
import com.assignment.mobiledata.data.RecordDisplayModel
import com.assignment.mobiledata.data.getVolumeOfDataInFloat
import com.assignment.mobiledata.data.getYear

class RecordUseCases(private val mobileDataRepo: IMobileDataRepository) : IRecordUseCases {

    override fun getYearlyRecordsLiveData() =
        Transformations.map(mobileDataRepo.getRecordsLiveData()) {
            recordsToDisplayModels(it)
        }

    override fun getErrorLiveData() = Transformations.map(mobileDataRepo.getErrorLiveData()) { it }

    override fun loadMobileData() = mobileDataRepo.loadMobileData()

    @VisibleForTesting
    fun recordsToDisplayModels(records: List<Record>): List<RecordDisplayModel> {
        val results = mutableListOf<RecordDisplayModel>()
        var year = ""
        var yearSum = 0f
        var prevData = 0f
        var downTrending = false

        for (record in records) {
            val curYear = record.getYear()
            if (curYear != year) {
                // add previous year to the list
                if (year.isNotEmpty()) {
                    results.add(RecordDisplayModel(yearSum.toString(), year, downTrending))
                }

                // reset data for a new year
                year = curYear
                yearSum = 0f
                downTrending = false
            }

            val curData = record.getVolumeOfDataInFloat()
            if (curData < prevData) {
                downTrending = true
            }
            yearSum += curData

            // set curData as prevData for next record
            prevData = curData
        }

        if (yearSum > 0) {
            results.add(RecordDisplayModel(yearSum.toString(), year, downTrending))
        }

        return filterWithinYearRange(results, START_YEAR, END_YEAR)
    }

    private fun filterWithinYearRange(
        list: List<RecordDisplayModel>,
        start: Int,
        end: Int
    ): List<RecordDisplayModel> {
        return list.filter { it.year.toInt() in start..end }
    }

    companion object {
        private const val START_YEAR = 2008
        private const val END_YEAR = 2018
    }

}
