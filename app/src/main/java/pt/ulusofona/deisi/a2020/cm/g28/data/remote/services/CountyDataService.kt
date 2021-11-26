package pt.ulusofona.deisi.a2020.cm.g28.data.remote.services

import pt.ulusofona.deisi.a2020.cm.g28.data.remote.responses.CountyDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CountyDataService {

    @GET("get_last_update_specific_county/{county}")
    suspend fun getCountyData(
        @Path("county")county : String) : Response<List<CountyDataResponse>>

    @GET("get_last_update_counties")
    suspend fun getAllCountiesData() : Response<List<CountyDataResponse>>

}