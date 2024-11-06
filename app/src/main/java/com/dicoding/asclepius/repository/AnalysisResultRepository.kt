package com.dicoding.asclepius.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.AnalysisResultDao
import com.dicoding.asclepius.data.AnalysisResultEntity
import com.dicoding.asclepius.data.AppDatabase

class AnalysisResultRepository(application: Application) {

    private val mAnalysisResultDao: AnalysisResultDao

    init {
        val db = AppDatabase.getDatabase(application)
        mAnalysisResultDao = db.analysisResultDao()
    }

    fun getAllAnalysisResult(): LiveData<List<AnalysisResultEntity>> = mAnalysisResultDao.getAllResults()
}
