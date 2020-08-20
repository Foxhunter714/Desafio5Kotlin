package cl.desafiolatam.desafiodos.orm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(@ColumnInfo(name = "text") var text: String){
    @PrimaryKey(autoGenerate = true)
    var uid: Int?  = null
}