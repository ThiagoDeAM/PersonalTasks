package com.example.personaltasks.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.adapter.DeletedTaskAdapter
import com.example.personaltasks.controller.DeletedTasksController
import com.example.personaltasks.databinding.ActivityDeletedTasksBinding
import com.example.personaltasks.model.Task
import kotlinx.coroutines.launch

// Tela responsável por exibir as tarefas marcadas como removidas
class DeletedTasksActivity : AppCompatActivity() {

    // Binding da Activity, utilizado para acessar os elementos da interface
    private val adtb by lazy {
        ActivityDeletedTasksBinding.inflate(layoutInflater)
    }

    // Lista local que armazena as tarefas excluídas
    private val deletedTaskList: MutableList<Task> = mutableListOf()

    // Controller responsável por buscar e atualizar tarefas no Firebase
    private val deletedController by lazy {
        DeletedTasksController()
    }

    // Adapter da RecyclerView que exibe a lista de tarefas excluídas
    private val deletedAdapter by lazy {
        DeletedTaskAdapter(this, deletedTaskList, deletedController, lifecycleScope)
    }

    companion object {
        const val GET_DELETED_TASKS_MESSAGE = 2 // Código identificador da mensagem
        const val GET_DELETED_TASKS_INTERVAL = 1000L // Intervalo de verificação (1 segundo)
    }

    // Handler responsável por chamar periodicamente o método de carregamento das tarefas excluídas
    private val getDeletedTasksHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == GET_DELETED_TASKS_MESSAGE) {
                loadDeletedTasks() // Recarrega as tarefas excluídas do Firebase
                // Agenda nova execução do Handler após o intervalo definido
                sendMessageDelayed(
                    obtainMessage(GET_DELETED_TASKS_MESSAGE),
                    GET_DELETED_TASKS_INTERVAL
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(adtb.root)

        // Configura a toolbar
        setSupportActionBar(adtb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Tarefas Excluídas"

        // Configura a RecyclerView
        adtb.deletedRv.adapter = deletedAdapter
        adtb.deletedRv.layoutManager = LinearLayoutManager(this)

        // Inicia o ciclo de verificação periódica das tarefas excluídas
        getDeletedTasksHandler.sendMessageDelayed(
            Message().apply { what = GET_DELETED_TASKS_MESSAGE },
            GET_DELETED_TASKS_INTERVAL
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove callbacks do Handler para evitar vazamento de memória
        getDeletedTasksHandler.removeCallbacksAndMessages(null)
    }

    // Carrega a lista de tarefas excluídas do controller e atualiza a RecyclerView
    private fun loadDeletedTasks() {
        lifecycleScope.launch {
            val deletedTasks = deletedController.getDeletedTasks()
            deletedTaskList.clear()
            deletedTaskList.addAll(deletedTasks)
            deletedAdapter.notifyDataSetChanged()
        }
    }
}