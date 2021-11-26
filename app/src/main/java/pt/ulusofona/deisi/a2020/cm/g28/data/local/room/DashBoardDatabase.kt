package pt.ulusofona.deisi.a2020.cm.g28.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.ulusofona.deisi.a2020.cm.g28.data.local.room.dao.DashBoardDao
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.DashBoardData

@Database(entities = arrayOf(DashBoardData::class), version = 1)
abstract class DashBoardDatabase : RoomDatabase(){

    abstract fun dashBoardDao(): DashBoardDao

    companion object{

        private var instance: DashBoardDatabase? = null

        fun getInstance(applicationContext: Context): DashBoardDatabase {

            synchronized(this){
                if(instance == null){
                    instance = Room.databaseBuilder(
                        applicationContext,
                        DashBoardDatabase::class.java,
                        "dashBoard_db"
                    ).build()
                }
                return instance as DashBoardDatabase
            }

        }

    }

}