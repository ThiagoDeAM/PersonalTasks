package com.example.personaltasks.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.R
import com.example.personaltasks.databinding.ActivityTaskBinding
import com.example.personaltasks.model.Constant.EXTRA_TASK
import com.example.personaltasks.model.Constant.EXTRA_VIEW_TASK
import com.example.personaltasks.model.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Activity responsável por adicionar, editar ou visualizar uma tarefa.
 * Utiliza ViewBinding para manipular os elementos da interface e DatePickerDialog
 * para seleção da data limite da tarefa.
 */
class TaskActivity : AppCompatActivity() {
    private val atb: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    // Data selecionada pelo usuário (via DatePickerDialog)
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

        // Se a task foi recebida, preenche os campos com os dados existentes
        receivedTask?.let {
            with(atb) {
                selectedDate = it.limitDate

                // Formata a data para exibição no botão
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                limitDateBt.text = sdf.format(selectedDate!!)

                // Preenche os campos de texto
                titleEt.setText(it.title)
                descriptionEt.setText(it.description)
                doneCb.isChecked = it.done

                //if ()

                //prioritySpinner.setSelection(getString(R.string.priority_spinner))

                priorityEt.setText(it.priority)

                // Verifica se está em modo de leitura
                val viewTask = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)
                if (viewTask) {
                    supportActionBar?.subtitle = "Visualizar tarefa"
                    titleEt.isEnabled = false
                    descriptionEt.isEnabled = false
                    limitDateBt.isEnabled = false
                    priorityEt.isEnabled = false
                    doneCb.isEnabled = false
                    saveBt.visibility = View.GONE
                    cancelBt.visibility = View.GONE
                }
            }
        }

        with(atb) {

            // Abre um DatePickerDialog para seleção de data
            limitDateBt.setOnClickListener {
                val calendar = Calendar.getInstance()
                selectedDate?.let { calendar.time = it }

                DatePickerDialog(
                    this@TaskActivity,
                    {_, year, month, dayOfMonth ->
                        calendar[Calendar.YEAR] = year
                        calendar[Calendar.MONTH] = month
                        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                        selectedDate = calendar.time
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        limitDateBt.text = sdf.format(selectedDate!!)
                    },
                    calendar[Calendar.YEAR],
                    calendar[Calendar.MONTH],
                    calendar[Calendar.DAY_OF_MONTH]
                ).show()
            }

            // Botão para salvar a tarefa
            saveBt.setOnClickListener {

                // Cria uma nova Task (ou atualiza a existente)
                Task (
                    id = receivedTask?.id?:hashCode(),
                    title = titleEt.text.toString(),
                    description = descriptionEt.text.toString(),
                    limitDate = selectedDate ?: Date(),
                    done = doneCb.isChecked,
                    deleted = receivedTask?.deleted ?: false,
                    priority = priorityEt.text.toString()

                    //priority = prioritySpinner.selectedItem.toString()

                ).let { task ->
                    // Retorna a tarefa como resultado para a MainActivity
                    Intent().apply {
                        putExtra(EXTRA_TASK, task)
                        setResult(RESULT_OK, this)
                    }
                }
                finish()
            }

            // Botão de cancelar (fecha a tela sem salvar)
            cancelBt.setOnClickListener {
                finish()
            }
        }
    }
}