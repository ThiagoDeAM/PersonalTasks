package com.example.personaltasks.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

import com.example.personaltasks.model.Constant.INVALID_TASK_ID
import java.util.Date


@Parcelize
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = INVALID_TASK_ID,
    var title: String = "",
    var description: String = "",
    var limitDate: Date

): Parcelable