package com.example.personaltasks.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Classe abstrata que representa o banco de dados local da aplicação.
 * É a implementação do RoomDatabase que fornece acesso ao DAO.
 * Define as entidades persistidas
 * Define a versão do banco
 */
@Database(entities = [Task::class], version = 1)
@TypeConverters(Converters::class) // Usado para tipos não primitivos
abstract class TaskRoomDb: RoomDatabase() {

    /**
     * Retorna a instância de TaskDao.
     * O Room gera automaticamente a implementação.
     */
    abstract fun taskDao():TaskDao
}