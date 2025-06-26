package com.example.personaltasks.controller

import com.example.personaltasks.model.Task
import com.example.personaltasks.model.TaskDao
import com.example.personaltasks.model.TaskFirebaseDatabase

// Controller responsável por gerenciar tarefas excluídas
// Interage com o DAO
class DeletedTasksController {

    /**
    private val taskDao: TaskDao = Room.databaseBuilder(
        context,
        TaskRoomDb::class.java,
        "task-database"
    ).build().taskDao() */

    private val taskDao: TaskDao = TaskFirebaseDatabase()

    /**
     * Recupera a lista de tarefas que estão marcadas como excluídas.
     * A busca é feita diretamente no Firebase via taskDao.
     */
    fun getDeletedTasks(): List<Task> = taskDao.getDeletedTasks()

    /**
     * Reativa uma tarefa excluída.
     * Atualiza campo 'deleted' para false e salva novamente no Firebase.
     */
    fun reactivateTask(task: Task) {
        task.deleted = false
        taskDao.updateTask(task)
    }

    /**
     * Marca uma tarefa como excluída.
     * Apenas atualiza o campo 'deleted' para true.
     */
    fun deleteTask(task: Task) {
        task.deleted = true
        taskDao.updateTask(task)
    }
}