package com.example.personaltasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.R
import com.example.personaltasks.databinding.TileTaskBinding
import com.example.personaltasks.model.Task
import com.example.personaltasks.ui.OnTaskClickListener
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Adapter personalizado para exibir a lista de tarefas usando RecyclerView.
 * Utiliza View Binding com o layout `tile_task.xml`.
 *
 * @param taskList Lista mutável de tarefas a serem exibidas
 * @param onTaskClickListener Listener para cliques e ações no item da tarefa
 */
class TaskRvAdapter(
    private val taskList: MutableList<Task>,
    private val onTaskClickListener: OnTaskClickListener
): RecyclerView.Adapter<TaskRvAdapter.TaskViewHolder>() {

    /**
     * ViewHolder interno que armazena as referências para os elementos da view
     * e configura os listeners de clique e menu de contexto.
     */
    inner class TaskViewHolder(ttb: TileTaskBinding): RecyclerView.ViewHolder(ttb.root) {
        val titleTv: TextView = ttb.titleTv
        val descriptionTv: TextView = ttb.descriptionTv
        val dateTv: TextView = ttb.dateTv

        init {
            // Define o menu de contexto (ao pressionar longamente no item)
            ttb.root.setOnCreateContextMenuListener { menu, _, _ ->
                (onTaskClickListener as AppCompatActivity).menuInflater.inflate(R.menu.context_menu_main, menu)
                menu.findItem(R.id.edit_task_mi).setOnMenuItemClickListener {
                    onTaskClickListener.onEditTaskMenuItemClick(adapterPosition)
                    true
                }
                menu.findItem(R.id.remove_task_mi).setOnMenuItemClickListener {
                    onTaskClickListener.onRemoveTaskMenuItemClick(adapterPosition)
                    true
                }
            }
            // Define ação de clique simples no item
            ttb.root.setOnClickListener{
                onTaskClickListener.onTaskClick(adapterPosition)
            }
        }
    }

    /**
     * Cria e infla o layout do item da RecyclerView.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder = TaskViewHolder(
        TileTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

  //Liga os dados da tarefa à view do ViewHolder.
  override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
      val task = taskList[position]
      with(holder) {
          // Atualiza título com o status
          val status = if (task.done) "[Realizada]" else "[Pendente]"
          val formattedTitle = itemView.context.getString(R.string.task_title_with_status, task.title, status)
          titleTv.text = formattedTitle

          descriptionTv.text = task.description

          val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
          dateTv.text = formatter.format(task.limitDate)
      }
  }

    // Retorna o número total de itens na lista.
    override fun getItemCount(): Int = taskList.size

}