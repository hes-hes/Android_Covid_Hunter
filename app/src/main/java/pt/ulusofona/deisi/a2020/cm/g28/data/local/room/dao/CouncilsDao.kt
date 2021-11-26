package pt.ulusofona.deisi.a2020.cm.g28.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.County

@Dao
interface CouncilsDao {

    @Insert
    suspend fun insertCouncils(councils : List<County>)

    @Query("SELECT * FROM county")
    suspend fun getAllCouncils(): List<County>

}