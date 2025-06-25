package com.example.personaltasks.model
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * DAO (Data Access Object) responsável pelas operações de acesso ao banco de dados
 * relacionadas à entidade Task. Utiliza as anotações do Room para mapear comandos SQL.
 */
@Dao
interface TaskDao {
    @Insert
    fun createTask(task: Task): Long
    @Query("SELECT * FROM Task WHERE id = :id")
    fun retrieveTask(id: Int): Task
    @Query("SELECT * FROM Task")
    fun retrieveTasks(): MutableList<Task>
    @Update
    fun updateTask(task: Task): Int
    @Delete
    fun deleteTask(task: Task): Int
    @Query("SELECT * FROM Task WHERE deleted = 1")
    fun getDeletedTasks(): List<Task>
    @Query("SELECT * FROM Task WHERE deleted = 0")
    fun retrieveActiveTasks(): List<Task>
}