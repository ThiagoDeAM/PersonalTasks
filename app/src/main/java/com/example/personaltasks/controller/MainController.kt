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

/**
 * Controlador responsável por intermediar o acesso entre a camada de UI (Activity)
 * e o banco de dados local (Room). Gerencia as operações de inserção, recuperação,
 * atualização e remoção de tarefas.
 *
 * @param mainActivity contexto da Activity principal, utilizado para instanciar o banco
 */
class MainController(mainActivity: MainActivity) {

    // Instancia o DAO de tarefas usando Room
    private val taskDao: TaskDao = Room.databaseBuilder(
        mainActivity,
        TaskRoomDb::class.java,
        "task-database"
    ).build().taskDao()

    /**
     * Insere uma nova tarefa no banco de dados utilizando corrotina
     * A operação é assíncrona para não travar a UI
     */
    fun insertTask(task: Task) {
        MainScope().launch {
            withContext(Dispatchers.IO) {
                taskDao.createTask(task)
            }
        }
    }

    /**
     * Recupera uma única tarefa pelo seu ID.
     * Executa em corrotina
     */
    fun getTask(id: Int) {
        MainScope().launch {
            withContext(Dispatchers.IO) {
                taskDao.retrieveTask(id)
            }
        }
    }

    /**
     * Recupera todas as tarefas salvas no banco de dados.
     * Executa em corrotina
     */
    fun getTasks() {
        MainScope().launch {
            withContext(Dispatchers.IO) {
                taskDao.retrieveTasks()
            }
        }
    }

    /**
     * Atualiza os dados de uma tarefa existente no banco.
     * Executa em segundo plano com corrotina.
     */
    fun modifyTask(task: Task) {
        MainScope().launch {
            withContext(Dispatchers.IO) {
                taskDao.updateTask(task)
            }
        }
    }

    /**
     * Remove uma tarefa do banco de dados
     * Executa em corrotina fora da thread principal
     */
    fun removeTask(task: Task) {
        MainScope().launch {
            withContext(Dispatchers.IO) {
                taskDao.deleteTask(task)
            }
        }
    }
}