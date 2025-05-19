package com.example.personaltasks.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.R
import com.example.personaltasks.databinding.ActivityTaskBinding
import com.example.personaltasks.model.Constant
import com.example.personaltasks.model.Constant.EXTRA_TASK
import com.example.personaltasks.model.Constant.EXTRA_VIEW_TASK
import com.example.personaltasks.model.Task

class TaskActivity : AppCompatActivity() {
    private val atb: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(atb.root)

        setSupportActionBar(atb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.task_list)

        val receivedTask = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
        }
        else {
            intent.getParcelableExtra<Task>(EXTRA_TASK)
        }
        receivedTask?.let {
            with(atb) {
                titleEt.setText(it.title)
                descriptionEt.setText(it.description)

                //limitDp

                val viewTask = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)
                if (viewTask) {
                    supportActionBar?.subtitle = "Visualizar tarefa"
                    titleEt.isEnabled = false
                    descriptionEt.isEnabled = false
                    //limitDp.isEnabled = false
                    saveBt.visibility = View.GONE
                    //cancelBt.visibility = View.GONE
                }
            }
        }
    }

}