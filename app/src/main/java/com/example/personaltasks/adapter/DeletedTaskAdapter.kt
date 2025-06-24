package com.example.personaltasks.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.databinding.TileTaskBinding
import com.example.personaltasks.model.Task

class DeletedTaskAdapter(
    private val tasks: MutableList<Task>
): RecyclerView.Adapter<DeletedTaskAdapter.DeletedTaskViewHolder>() {
    inner class DeletedTaskViewHolder(val ttb: TileTaskBinding) :
        RecyclerView.ViewHolder(ttb.root), View.OnCreateContextMenuListener {
            init {
                ttb.root.setOnCreateContextMenuListener(this)
            }

        }
}