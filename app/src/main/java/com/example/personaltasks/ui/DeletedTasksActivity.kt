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

class DeletedTasksActivity : AppCompatActivity() {

    private val adtb by lazy {
        ActivityDeletedTasksBinding.inflate(layoutInflater)
    }

    private val deletedTaskList: MutableList<Task> = mutableListOf()

    private val deletedController by lazy {
        DeletedTasksController()
    }

    private val deletedAdapter by lazy {
        DeletedTaskAdapter(this, deletedTaskList, deletedController, lifecycleScope)
    }

    companion object {
        const val GET_DELETED_TASKS_MESSAGE = 2
        const val GET_DELETED_TASKS_INTERVAL = 1000L
    }

    private val getDeletedTasksHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == GET_DELETED_TASKS_MESSAGE) {
                loadDeletedTasks()
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

        setSupportActionBar(adtb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Tarefas Excluídas"

        adtb.deletedRv.adapter = deletedAdapter
        adtb.deletedRv.layoutManager = LinearLayoutManager(this)

        getDeletedTasksHandler.sendMessageDelayed(
            Message().apply { what = GET_DELETED_TASKS_MESSAGE },
            GET_DELETED_TASKS_INTERVAL
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        getDeletedTasksHandler.removeCallbacksAndMessages(null)
    }

    private fun loadDeletedTasks() {
        lifecycleScope.launch {
            val deletedTasks = deletedController.getDeletedTasks()
            deletedTaskList.clear()
            deletedTaskList.addAll(deletedTasks)
            deletedAdapter.notifyDataSetChanged()
        }
    }
}