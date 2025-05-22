package com.example.personaltasks.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.personaltasks.R
import com.example.personaltasks.databinding.TileTaskBinding
import com.example.personaltasks.model.Task
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Adapter responsável por exibir a lista de tarefas no formato de `ListView`.
 * Utiliza um layout customizado `tile_task.xml` para cada item.
 *
 * @param context contexto da aplicação
 * @param taskList lista de tarefas a serem exibidas
 */
class TaskAdapter(context: Context, private val taskList: MutableList<Task>):
    ArrayAdapter<Task>(
        context,
        R.layout.tile_task,
        taskList
    ) {
    /**
     * Método chamado para gerar a visualização de cada item da lista.
     * Usa ViewHolder para reaproveitar views.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task = taskList[position] // tarefa atual a ser exibida

        var taskTileView = convertView

        // Se a view ainda não foi criada, o layout é infladop e o ViewHolder é inicializado
        if(taskTileView == null) {
            taskTileView = TileTaskBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            ).root

            // Cria o ViewHolder com referências aos TextViews do layout
            val tileTaskViewHolder = TileTaskViewHolder(
                taskTileView.findViewById(R.id.title_tv),
                taskTileView.findViewById(R.id.description_tv),
                taskTileView.findViewById(R.id.date_tv)
            )

            // Armazena o ViewHolder como tag da view
            taskTileView.tag = tileTaskViewHolder
        }

        // Recupera o ViewHolder existente
        val viewHolder = taskTileView.tag as TileTaskViewHolder

        // Define os valores nos TextViews a partir do objeto Task
        viewHolder.titleTv.text = task.title
        viewHolder.descriptionTv.text = task.description

        // Formata a data para exibição no padrão dd/MM/yyyy
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        viewHolder.dateTv.text = formatter.format(task.limitDate)

        return taskTileView
    }

    // ViewHolder interno para armazenar as referências dos TextViews de cada item.
    private data class TileTaskViewHolder(val titleTv: TextView, val descriptionTv: TextView, val dateTv: TextView)
}