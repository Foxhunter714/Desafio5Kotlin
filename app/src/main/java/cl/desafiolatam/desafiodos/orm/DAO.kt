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
    @Query("SELECT * FROM task WHERE uid = (:id)")
    fun loadById(id: Long): List<Task>
    @Query("UPDATE task SET text=(:text) WHERE uid = (:id)")
    fun update(text: String, id: Long?)
    @Insert
    fun insert(task: Task)
    @Delete
    fun delete(task: Task)
    @Query("DELETE FROM task")
    fun delete()
}