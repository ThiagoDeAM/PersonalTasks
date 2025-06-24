package com.example.personaltasks.controller

import android.content.Context
import androidx.room.Room
import com.example.personaltasks.model.TaskDao
import com.example.personaltasks.model.TaskRoomDb

class DeletedTasksController(context: Context) {

    private val taskDao: TaskDao = Room.databaseBuilder(
        context,
        TaskRoomDb::class.java,
        "task-database"
    ).build().taskDao()

}