package com.example.personaltasks.controller

import com.example.personaltasks.model.Task
import com.example.personaltasks.model.TaskDao
import com.example.personaltasks.model.TaskFirebaseDatabase

class DeletedTasksController {

    /**
    private val taskDao: TaskDao = Room.databaseBuilder(
        context,
        TaskRoomDb::class.java,
        "task-database"
    ).build().taskDao() */

    private val taskDao: TaskDao = TaskFirebaseDatabase()

    fun getDeletedTasks(): List<Task> = taskDao.getDeletedTasks()

    fun reactivateTask(task: Task) {
        task.deleted = false
        taskDao.updateTask(task)
    }

    fun deleteTask(task: Task) {
        task.deleted = true
        taskDao.updateTask(task)
    }
}