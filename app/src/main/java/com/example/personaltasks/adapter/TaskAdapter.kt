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

class TaskAdapter(context: Context, private val taskList: MutableList<Task>):
    ArrayAdapter<Task>(
        context,
        R.layout.tile_task,
        taskList
    ) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task = taskList[position]
        var taskTileView = convertView
        if(taskTileView == null) {
            taskTileView = TileTaskBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            ).root

            val tileTaskViewHolder = TileTaskViewHolder(
                taskTileView.findViewById(R.id.title_tv),
                taskTileView.findViewById(R.id.description_tv),
                taskTileView.findViewById(R.id.date_tv)
            )

            taskTileView.tag = tileTaskViewHolder
        }

        val viewHolder = taskTileView.tag as TileTaskViewHolder
        viewHolder.titleTv.text = task.title
        viewHolder.descriptionTv.text = task.description
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        viewHolder.dateTv.text = formatter.format(task.limitDate)

        return taskTileView
    }

    private data class TileTaskViewHolder(val titleTv: TextView, val descriptionTv: TextView, val dateTv: TextView)
}