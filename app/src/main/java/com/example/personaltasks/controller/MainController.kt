package com.example.personaltasks.controller

import android.os.Message
import com.example.personaltasks.model.Constant.EXTRA_TASK_ARRAY
import com.example.personaltasks.model.Task
import com.example.personaltasks.model.TaskDao
import com.example.personaltasks.model.TaskFirebaseDatabase
import com.example.personaltasks.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    fun insertTask(task: Task) {
        databaseCoroutineScope.launch {
            taskDao.createTask(task)
        }
    }

    /**
     * Recupera uma única tarefa pelo seu ID.
     */
    fun getTask(id: Int) = databaseCoroutineScope.launch {
        taskDao.retrieveTask(id)
    }

    /**
     * Recupera todas as tarefas salvas no banco de dados.
     */
    fun getTasks() {
        databaseCoroutineScope.launch {
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
    fun modifyTask(task: Task) {
        databaseCoroutineScope.launch {
            taskDao.updateTask(task)
        }
    }

    /**
     * Remove logicamente a tarefa
     */
    fun removeTask(task: Task) {
        /**
        MainScope().launch {
        withContext(Dispatchers.IO) {
        taskDao.deleteTask(task)
        }
        } */
        databaseCoroutineScope.launch {
            task.deleted = true
            taskDao.updateTask(task)
        }
    }
}