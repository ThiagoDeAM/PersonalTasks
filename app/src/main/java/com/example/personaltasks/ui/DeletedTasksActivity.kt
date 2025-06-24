package com.example.personaltasks.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.R
import com.example.personaltasks.adapter.DeletedTaskAdapter
import com.example.personaltasks.controller.DeletedTasksController
import com.example.personaltasks.databinding.ActivityDeletedTasksBinding
import com.example.personaltasks.model.Task

class DeletedTasksActivity : AppCompatActivity() {

    private val adtb by lazy {
        ActivityDeletedTasksBinding.inflate(layoutInflater)
    }

    private val deletedTaskList: MutableList<Task> = mutableListOf()

    private val deletedController by lazy {
        DeletedTasksController(this)
    }

    private val deletedAdapter by lazy {
        DeletedTaskAdapter(this, deletedTaskList, deletedController, lifecycleScope)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(adtb.root)

        setSupportActionBar(adtb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Tarefas Excluídas"

        adtb.deletedRv.adapter = deletedAdapter
        adtb.deletedRv.layoutManager = LinearLayoutManager(this)



    }
}