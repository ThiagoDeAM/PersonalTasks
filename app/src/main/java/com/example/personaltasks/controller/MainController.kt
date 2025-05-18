package com.example.personaltasks.controller

import androidx.room.Room
import com.example.personaltasks.model.Task
import com.example.personaltasks.model.TaskDao
import com.example.personaltasks.model.TaskRoomDb
import com.example.personaltasks.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainController(mainActivity: MainActivity) {

    private val taskDao: TaskDao = Room.databaseBuilder(
        mainActivity,
        TaskRoomDb::class.java,
        "task-database"
    ).build().taskDao()

    fun insertTask(task: Task) {
        MainScope().launch {
            withContext(Dispatchers.IO) {
                taskDao.createTask(task)
            }
        }
    }
    fun getTask(id: Int) = taskDao.retrieveTask(id)
    fun getTasks() = taskDao.retrieveTasks()
    fun modifyTask(task: Task) {
        MainScope().launch {
            withContext(Dispatchers.IO) {
                taskDao.updateTask(task)
            }
        }
    }

    fun removeTask(task: Task) {
        MainScope().launch {
            withContext(Dispatchers.IO) {
                taskDao.deleteTask(task)
            }
        }
    }


}