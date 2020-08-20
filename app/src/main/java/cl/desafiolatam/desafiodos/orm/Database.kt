package cl.desafiolatam.desafiodos.orm

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities= [Task::class], version = 1)
public abstract class UserRoomDatabase: RoomDatabase(){
    abstract fun idDao(): DAO

    companion object{

        @Volatile
        private var INSTANCE: UserRoomDatabase? = null

        fun getDatabase(context: Context): UserRoomDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext, UserRoomDatabase::class.java, "word_database").allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}