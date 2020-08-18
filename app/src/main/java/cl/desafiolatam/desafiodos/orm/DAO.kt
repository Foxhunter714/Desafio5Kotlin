package cl.desafiolatam.desafiodos.orm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cl.desafiolatam.desafiodos.task.TaskUIDataHolder

@Dao
interface DAO {
    @Query("SELECT * FROM task")
    fun getAll(): List<Task>
    @Query("SELECT * FROM task WHERE id = id")
    fun loadById(id: Long): List<Task>
    @Insert
    fun insert(task: Task)
    @Delete
    fun delete(task: Task)
}