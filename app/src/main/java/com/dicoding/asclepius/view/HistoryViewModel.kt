package com.dicoding.asclepius.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.AnalysisResultEntity
import com.dicoding.asclepius.repository.AnalysisResultRepository

class HistoryViewModel (application: Application) : AndroidViewModel(application) {
    private val mAnalysisResultRepository: AnalysisResultRepository = AnalysisResultRepository(application)
    private val mAllResults: LiveData<List<AnalysisResultEntity>> = mAnalysisResultRepository.getAllAnalysisResult()

    fun getAllResults(): LiveData<List<AnalysisResultEntity>> {
        return mAllResults
    }
}