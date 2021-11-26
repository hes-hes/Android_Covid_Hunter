package pt.ulusofona.deisi.a2020.cm.g28.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.CovidTest

@Dao
interface CovidTestDao {

    @Insert
    suspend fun insertTest(covidTest: CovidTest)

    @Query("SELECT * FROM covidTest")
    suspend fun getAllTest(): List<CovidTest>

    @Query("DELETE FROM covidTest WHERE uuid = :uuid")
    suspend fun deleteTest(uuid: String)

    @Query("SELECT * FROM covidTest WHERE uuid = :uuid")
    suspend fun getTestById(uuid: String): CovidTest

}