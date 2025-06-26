package com.example.personaltasks.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.R
import com.example.personaltasks.controller.DeletedTasksController
import com.example.personaltasks.databinding.TileTaskBinding
import com.example.personaltasks.model.Constant.EXTRA_TASK
import com.example.personaltasks.model.Constant.EXTRA_VIEW_TASK
import com.example.personaltasks.model.Task
import com.example.personaltasks.ui.TaskActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

// Adapter responsável por exibir tarefas excluídas em uma RecyclerView
class DeletedTaskAdapter(
    private val context: Context,
    private val tasks: MutableList<Task>,
    private val deletedController: DeletedTasksController,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): RecyclerView.Adapter<DeletedTaskAdapter.DeletedTaskViewHolder>() {

    //ViewHolder que encapsula o layout de um item da lista
    inner class DeletedTaskViewHolder(val ttb: TileTaskBinding) :
        RecyclerView.ViewHolder(ttb.root), View.OnCreateContextMenuListener {
        init {
            // Define o listener de menu de contexto
            ttb.root.setOnCreateContextMenuListener(this)
        }

            override fun onCreateContextMenu(
                menu: ContextMenu?,
                v: View?,
                menuInfo: ContextMenu.ContextMenuInfo?
            ) {
                menuInflater().inflate(R.menu.context_menu_deleted, menu)

                // Ação: Reativar Tarefa
                menu?.findItem(R.id.reativar_task_mi)?.setOnMenuItemClickListener {
                    val position = adapterPosition
                    val task = tasks[position]
                    lifecycleScope.launch {
                        withContext(dispatcher) {
                            deletedController.reactivateTask(task)
                        }
                        tasks.removeAt(position)
                        notifyItemRemoved(position)
                        (context as AppCompatActivity).setResult(Activity.RESULT_OK)
                        Toast.makeText(context,
                            context.getString(R.string.task_reactivated), Toast.LENGTH_SHORT).show()
                    }
                    true
                }

                // Ação: Visualizar detalhes da tarefa
                menu?.findItem(R.id.detalhes_task_mi)?.setOnMenuItemClickListener {
                    val task = tasks[adapterPosition]
                    val intent = Intent(context, TaskActivity::class.java).apply {
                        putExtra(EXTRA_TASK, task)
                        putExtra(EXTRA_VIEW_TASK, true)
                    }
                    context.startActivity(intent)
                    true
                }
            }

        // Infla menus usando o contexto da Activity
        private fun menuInflater(): MenuInflater {
            return MenuInflater(context)
        }
    }

    // Cria o ViewHolder com base no layout do item da lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeletedTaskViewHolder {
        val ttb = TileTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeletedTaskViewHolder(ttb)
    }

    // Associa os dados da tarefa com os elementos de interface
    override fun onBindViewHolder(holder: DeletedTaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.ttb.titleTv.text = task.title
        holder.ttb.descriptionTv.text = task.description

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.ttb.dateTv.text = sdf.format(task.limitDate)
    }

    // Retorna o número total de itens na lista
    override fun getItemCount(): Int = tasks.size
}