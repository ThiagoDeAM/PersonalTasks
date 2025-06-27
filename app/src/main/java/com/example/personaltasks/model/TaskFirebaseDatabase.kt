package com.example.personaltasks.model

import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

// Implementação da interface TaskDao usando Firebase Realtime Database
class TaskFirebaseDatabase: TaskDao {

    // Referência à raiz da lista de tarefas no Firebase
    private val databaseReference = Firebase.database.getReference("taskList")

    // Lista local de tarefas que é sincronizada com o banco em tempo real
    private val taskList = mutableListOf<Task>()

    init {
        // Escuta eventos de adição, alteração e remoção no nó "taskList"
        databaseReference.addChildEventListener(object : ChildEventListener {

            // Quando uma nova tarefa é adicionada no Firebase
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val task = snapshot.getValue<Task>()
                task?.let { newTask ->
                    // Adiciona à lista local apenas se ainda não existir
                    if (!taskList.any {it.id == newTask.id}) {
                        taskList.add(newTask)
                    }
                }
            }

            // Quando uma tarefa existente é modificada no Firebase
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val task = snapshot.getValue<Task>()
                task?.let { editedTask ->
                    val index = taskList.indexOfFirst { it.id == editedTask.id }
                    if (index != -1) {
                        taskList[index] = editedTask
                    }
                    else {
                        taskList.add(editedTask)
                    }
                }
            }

            // Quando uma tarefa é removida do Firebase
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val task = snapshot.getValue<Task>()
                task?.let { taskList.remove(it) }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // NSA
            }

            override fun onCancelled(error: DatabaseError) {
                // NSA
            }
        })

        // Carrega a lista de tarefas inicialmente
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskMap = snapshot.getValue<Map<String, Task>>()
                taskList.clear()
                taskMap?.values?.also {
                    taskList.addAll(it) // Popula a lista com as tarefas do Firebase
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // NSA
            }
        })
    }

    // Cria uma nova tarefa no Firebase
    override fun createTask(task: Task): Long {
        databaseReference.child(task.id.toString()).setValue(task)
        return 1L
    }

    // Recupera uma tarefa pelo id a partir da lista local
    override fun retrieveTask(id: Int) =
        taskList[taskList.indexOfFirst { it.id == id }]

    // Retorna uma cópia da lista local de tarefas
    override fun retrieveTasks(): MutableList<Task> {
        return taskList.toMutableList()
    }

    //Atualiza uma tarefa existente no Firebase
    override fun updateTask(task: Task): Int {
        databaseReference.child(task.id.toString()).setValue(task)
        return 1
    }

    // Marca uma tarefa como deletada no Firebase
    override fun deleteTask(task: Task): Int {
        task.deleted = true
        databaseReference.child(task.id.toString()).setValue(task)
        return 1
    }

    // Retorna somente as tarefas marcadas como deletadas
    override fun getDeletedTasks(): List<Task> {
        return taskList.filter { it.deleted }.toList()
    }

    // Retorna apenas as tarefas ativas
    override fun retrieveActiveTasks(): List<Task> {
        return taskList.filter { !it.deleted }
    }
}