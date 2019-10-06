package com.assignment.mobiledata.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.assignment.mobiledata.data.Record

/**
 * The Room Database that contains the [Record] table.
 *
 * Note that exportSchema should be true in production databases.
 */
@Database(entities = [Record::class], version = 1, exportSchema = false)
abstract class RecordDatabase : RoomDatabase() {

    abstract fun recordDao(): RecordDao
}
