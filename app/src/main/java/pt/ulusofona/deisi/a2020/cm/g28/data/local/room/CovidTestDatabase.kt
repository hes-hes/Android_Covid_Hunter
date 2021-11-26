package pt.ulusofona.deisi.a2020.cm.g28.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.ulusofona.deisi.a2020.cm.g28.data.local.room.dao.CovidTestDao
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.CovidTest

@Database(entities = arrayOf(CovidTest::class), version = 1)
abstract class CovidTestDatabase : RoomDatabase() {

    abstract fun covidTestDao(): CovidTestDao

    companion object{

        private var instance: CovidTestDatabase? = null

        fun getInstance(applicationContext: Context): CovidTestDatabase {
            synchronized(this){
                if(instance == null){
                    instance = Room.databaseBuilder(
                        applicationContext,
                        CovidTestDatabase::class.java,
                        "covidTest_db"
                    ).build()
                }
                return instance as CovidTestDatabase
            }
        }

    }

}