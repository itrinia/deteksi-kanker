package com.dicoding.asclepius.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "analysis_results")
@Parcelize
data class AnalysisResultEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")val id: Long = 0,
    @ColumnInfo(name = "photo")val imageUri: String,
    @ColumnInfo(name = "date") var date: String? = null,
    @ColumnInfo(name = "result")val result: String
) : Parcelable
