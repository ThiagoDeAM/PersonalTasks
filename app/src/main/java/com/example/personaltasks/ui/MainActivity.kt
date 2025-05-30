package com.example.personaltasks.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.R
import com.example.personaltasks.adapter.TaskRvAdapter
import com.example.personaltasks.controller.MainController
import com.example.personaltasks.databinding.ActivityMainBinding
import com.example.personaltasks.model.Constant.EXTRA_TASK
import com.example.personaltasks.model.Constant.EXTRA_VIEW_TASK
import com.example.personaltasks.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Responsável por exibir a lista de tarefas e permitir sua criação, edição e remoção
 * Utiliza RecyclerView com um adapter e Room para persistência local.
 */
class MainActivity : AppCompatActivity(), OnTaskClickListener {

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //Data Source: lista mutável que armazena as tarefas
    private val taskList: MutableList<Task> = mutableListOf()

    //Adapter que exibe as tarefas na RecyclerView
    private val taskAdapter: TaskRvAdapter by lazy {
        TaskRvAdapter(taskList, this)
    }

    // Launcher para lidar com retorno de activities
    private lateinit var carl: ActivityResultLauncher<Intent>

    //Controller responsável por manipular as operações com o BD
    private val mainController: MainController by lazy {
        MainController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        // Configura a toolbar
        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.task_list)

        // Inicializa o launcher para TaskActivity (criação/edição de tarefa)
        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if (result.resultCode == RESULT_OK){
                val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    result.data?.getParcelableExtra(EXTRA_TASK, Task::class.java)
                }
                else {
                    result.data?.getParcelableExtra<Task>(EXTRA_TASK)
                }
                task?.let { receivedTask ->
                    val position = taskList.indexOfFirst { it.id == receivedTask.id }
                    if (position == -1) {
                        // Nova tarefa -> adiciona à lista e inssere no banco
                        taskList.add(receivedTask)
                        taskAdapter.notifyItemInserted(taskList.lastIndex)
                        mainController.insertTask(receivedTask)
                    }
                    else {
                        // Edição de tarefa existente
                        taskList[position] = receivedTask
                        taskAdapter.notifyItemChanged(position)
                        mainController.modifyTask(receivedTask)
                    }
                }
            }
        }

        // Configura o RecyclerView
        amb.taskRv.adapter = taskAdapter
        amb.taskRv.layoutManager = LinearLayoutManager(this)

        // Carrega as tarefas persistidas do banco
        fillContactList()
    }

    // Infla o menu superior
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Lida com cliques no menu da Toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.add_task_mi -> {
                carl.launch(Intent(this, TaskActivity::class.java))
                true
            }
            else -> { false }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * Ação ao clicar em um item da lista para visualizar a tarefa.
     * Abre a TaskActivity em modo de leitura.
     */
    override fun onTaskClick(position: Int) {
        Intent(this, TaskActivity::class.java).apply {
            putExtra(EXTRA_TASK, taskList[position])
            putExtra(EXTRA_VIEW_TASK, true)
            startActivity(this)
        }
    }

    /**
     * Ação ao selecionar "Remover" no menu de contexto da tarefa.
     * Remove a tarefa da lista e do BD
     */
    override fun onRemoveTaskMenuItemClick(position: Int) {
        mainController.removeTask(taskList[position])
        taskList.removeAt(position)
        taskAdapter.notifyItemRemoved(position)
        Toast.makeText(this, "Tarefa removida!", Toast.LENGTH_SHORT).show()
    }

    /**
     * Ação ao selecionar "Editar" no menu de contexto da tarefa.
     * Abre a TaskActivity com a tarefa para edição.
     */
    override fun onEditTaskMenuItemClick(position: Int) {
        Intent(this, TaskActivity::class.java).apply {
            putExtra(EXTRA_TASK, taskList[position])
            carl.launch(this)
        }
    }

    /**
     * Preenche a RecyclerView com as tarefas salvas no BD.
     * A operação de leitura utiliza corrotinas.
     */
    private fun fillContactList() {
        lifecycleScope.launch {
            val tasks = withContext(Dispatchers.IO) {
                mainController.getTasks()
            }
            taskList.clear()
            taskList.addAll(tasks)
            taskAdapter.notifyDataSetChanged()
        }
    }
}