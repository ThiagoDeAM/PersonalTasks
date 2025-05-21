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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class TaskActivity : AppCompatActivity() {
    private val atb: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    private var selectedDate: Date? = null

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
                selectedDate = it.limitDate
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                limitDateBt.text = sdf.format(selectedDate!!)
                titleEt.setText(it.title)
                descriptionEt.setText(it.description)

                val viewTask = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)
                if (viewTask) {
                    supportActionBar?.subtitle = "Visualizar tarefa"
                    titleEt.isEnabled = false
                    descriptionEt.isEnabled = false
                    limitDp.isEnabled = false
                    saveBt.visibility = View.GONE
                    cancelBt.visibility = View.GONE
                }
            }
        }

        with(atb) {
            saveBt.setOnClickListener {
                val calendar = Calendar.getInstance()
                calendar.set(
                    limitDp.year,
                    limitDp.month,
                    limitDp.dayOfMonth
                )
                val selectedDate = calendar.time

                Task (
                    id = receivedTask?.id?:hashCode(),
                    title = titleEt.text.toString(),
                    description = descriptionEt.text.toString(),
                    limitDate = selectedDate
                ).let { task ->
                    Intent().apply {
                        putExtra(EXTRA_TASK, task)
                        setResult(RESULT_OK, this)
                    }
                }
                finish()
            }

            cancelBt.setOnClickListener {
                finish()
            }
        }
    }

}