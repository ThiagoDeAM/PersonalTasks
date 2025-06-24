package com.example.personaltasks.adapter

import android.content.Context
import android.content.Intent
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.R
import com.example.personaltasks.controller.DeletedTasksController
import com.example.personaltasks.databinding.TileTaskBinding
import com.example.personaltasks.model.Constant.EXTRA_TASK
import com.example.personaltasks.model.Constant.EXTRA_VIEW_TASK
import com.example.personaltasks.model.Task
import com.example.personaltasks.ui.TaskActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeletedTaskAdapter(
    private val context: Context,
    private val tasks: MutableList<Task>,
    private val deletedController: DeletedTasksController,
    private val lifecycleScope: LifecycleCoroutineScope
): RecyclerView.Adapter<DeletedTaskAdapter.DeletedTaskViewHolder>() {
    inner class DeletedTaskViewHolder(val ttb: TileTaskBinding) :
        RecyclerView.ViewHolder(ttb.root), View.OnCreateContextMenuListener {
        init {
            ttb.root.setOnCreateContextMenuListener(this)
        }

            override fun onCreateContextMenu(
                menu: ContextMenu?,
                v: View?,
                menuInfo: ContextMenu.ContextMenuInfo?
            ) {
                menuInflater().inflate(R.menu.context_menu_deleted, menu)

                menu?.findItem(R.id.reativar_task_mi)?.setOnMenuItemClickListener {
                    val task = tasks[adapterPosition]
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {

                        }
                        tasks.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                        Toast.makeText(context, "Tarefa reativada", Toast.LENGTH_SHORT).show()
                    }
                    true
                }

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

        private fun menuInflater(): MenuInflater {
            return MenuInflater(context)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeletedTaskViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: DeletedTaskViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}