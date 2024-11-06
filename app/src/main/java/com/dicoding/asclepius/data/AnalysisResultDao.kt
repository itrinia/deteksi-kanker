package com.dicoding.asclepius.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AnalysisResultDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    fun insertResult(result: AnalysisResultEntity)

    @Query("SELECT * FROM analysis_results ORDER BY id DESC")
    fun getAllResults(): LiveData<List<AnalysisResultEntity>>
}