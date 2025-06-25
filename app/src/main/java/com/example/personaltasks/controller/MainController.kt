package com.example.personaltasks.controller

import android.os.Message
import androidx.room.Room
import com.example.personaltasks.model.Constant.EXTRA_TASK_ARRAY
import com.example.personaltasks.model.Task
import com.example.personaltasks.model.TaskDao
import com.example.personaltasks.model.TaskFirebaseDatabase
import com.example.personaltasks.model.TaskRoomDb
import com.example.personaltasks.ui.MainActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Controlador responsável por intermediar o acesso entre a camada de UI (Activity)
 * e o banco de dados local (Room). Gerencia as operações de inserção, recuperação,
 * atualização e remoção de tarefas.
 *
 * @param mainActivity contexto da Activity principal, utilizado para instanciar o banco
 */
class MainController(private val mainActivity: MainActivity) {

    // Instancia o DAO de tarefas usando Room
    /**
    private val taskDao: TaskDao = Room.databaseBuilder(
        mainActivity,
        TaskRoomDb::class.java,
        "task-database"
    ).build().taskDao()*/

    private val taskDao: TaskDao = TaskFirebaseDatabase()
    private val databaseCoroutineScope = CoroutineScope(Dispatchers.IO)

    /**
     * Insere uma nova tarefa no banco de dados utilizando corrotina
     * A operação é assíncrona para não travar a UI
     */
    suspend fun insertTask(task: Task, ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {
        withContext(ioDispatcher) {
            taskDao.createTask(task)
        }
    }

    /**
     * Recupera uma única tarefa pelo seu ID.
     */
    fun getTask(id: Int) = taskDao.retrieveTask(id)

    /**
     * Recupera todas as tarefas salvas no banco de dados.
     */
    suspend fun getTasks(ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {
        return withContext(ioDispatcher) {
            val taskList = taskDao.retrieveActiveTasks()
            mainActivity.getTasksHandler.sendMessage(Message().apply {
                data.putParcelableArray(EXTRA_TASK_ARRAY, taskList.toTypedArray())
            })
        }
    }

    /**
     * Atualiza os dados de uma tarefa existente no banco.
     * Executa em segundo plano com corrotina.
     */
    suspend fun modifyTask(task: Task, ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {
        withContext(ioDispatcher) {
            taskDao.updateTask(task)
        }
    }

    /**
     * Remove logicamente a tarefa
     */
    suspend fun removeTask(task: Task, ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {
        /**
        MainScope().launch {
        withContext(Dispatchers.IO) {
        taskDao.deleteTask(task)
        }
        } */
        withContext(ioDispatcher) {
            task.deleted = true
            taskDao.updateTask(task)
        }
    }
}