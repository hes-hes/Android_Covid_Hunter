package pt.ulusofona.deisi.a2020.cm.g28.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.ulusofona.deisi.a2020.cm.g28.data.local.room.dao.CouncilsDao
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.County

@Database(entities = arrayOf(County::class), version = 1)

abstract class CountyDatabase : RoomDatabase() {

    abstract fun councilsDao(): CouncilsDao

    companion object{

        private var instance: CountyDatabase? = null

        fun getInstance(applicationContext: Context): CountyDatabase {
            synchronized(this){
                if(instance == null){
                    instance = Room.databaseBuilder(
                        applicationContext,
                        CountyDatabase::class.java,
                        "councils_db"
                    ).build()
                }
                return instance as CountyDatabase
            }
        }

    }

}