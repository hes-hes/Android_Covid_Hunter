package pt.ulusofona.deisi.a2020.cm.g28.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.DashBoardData

@Dao
interface DashBoardDao {

    @Insert
    suspend fun updateDashBoard(dashBoardData: DashBoardData)

    @Query("SELECT * FROM dashBoardData")
    suspend fun getAllDashBoardData(): List<DashBoardData>

    @Query("DELETE FROM dashBoardData")
    suspend fun clearDataBase()

}
