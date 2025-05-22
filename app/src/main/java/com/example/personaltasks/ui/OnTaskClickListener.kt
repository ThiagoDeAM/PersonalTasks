package com.example.personaltasks.ui

/**
 * Interface responsável por definir os comportamentos de clique em tarefas.
 */
sealed interface OnTaskClickListener {
    // Chamado quando o usuário clica em um item da lista de tarefas.
    fun onTaskClick(position: Int)

    // Chamado quando o usuário seleciona a opção "Remover" no menu de contexto.
    fun onRemoveTaskMenuItemClick(position: Int)

    // Chamado quando o usuário seleciona a opção "Editar" no menu de contexto.
    fun onEditTaskMenuItemClick(position: Int)
}