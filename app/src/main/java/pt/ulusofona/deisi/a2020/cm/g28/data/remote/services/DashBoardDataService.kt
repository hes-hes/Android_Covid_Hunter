package pt.ulusofona.deisi.a2020.cm.g28.data.remote.services

import pt.ulusofona.deisi.a2020.cm.g28.data.remote.responses.DashBoardDataResponse
import retrofit2.Response
import retrofit2.http.GET

interface DashBoardDataService {

    @GET("get_last_update")
    suspend fun getUpdatedData() : Response<DashBoardDataResponse>

}