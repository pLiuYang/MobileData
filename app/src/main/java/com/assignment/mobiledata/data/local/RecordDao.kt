package com.assignment.mobiledata.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.assignment.mobiledata.data.Record

@Dao
interface RecordDao {

    /**
     * Get all records from database.
     */
    @Query("SELECT * FROM records")
    fun getAllRecords(): List<Record>

    /**
     * Save all records into database.
     */
    @Insert
    fun insertAllRecords(records: List<Record>)

    /**
     * Delete all records.
     */
    @Query("DELETE FROM records")
    fun deleteAllRecords()
}
