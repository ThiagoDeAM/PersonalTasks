package com.example.personaltasks.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personaltasks.R
import com.example.personaltasks.databinding.ActivityDeletedTasksBinding
import com.example.personaltasks.model.Task

class DeletedTasksActivity : AppCompatActivity() {

    private val adtb by lazy {
        ActivityDeletedTasksBinding.inflate(layoutInflater)
    }
    private val deletedTaskList: MutableList<Task> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(adtb.root)

        setSupportActionBar(adtb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Tarefas Excluídas"


    }
}