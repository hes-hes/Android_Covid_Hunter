package pt.ulusofona.deisi.a2020.cm.g28.domain.logic

import kotlinx.coroutines.runBlocking
import pt.ulusofona.deisi.a2020.cm.g28.data.local.room.dao.CouncilsDao
import pt.ulusofona.deisi.a2020.cm.g28.data.remote.responses.CountyDataResponse
import pt.ulusofona.deisi.a2020.cm.g28.data.remote.services.CountyDataService
import pt.ulusofona.deisi.a2020.cm.g28.data.repositories.CouncilsRepository
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.County
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals
import retrofit2.Response

class CountyListLogic(private val repository : CouncilsRepository) {

    fun getCounties() : ArrayList<County>{

        var councils : ArrayList<County> = ArrayList()

        if(Globals.INTERNET_CONNECTION) {

            val service = repository.remote.create(CountyDataService::class.java)
            val response = getCounties(service)

            if (response.isSuccessful && response.body() != null){

                var index = 0
                while (index < response.body()!!.size) {
                    councils.add(
                        County(
                            response.body()!![index].concelho,
                            response.body()!![index].distrito,
                            response.body()!![index].incidencia_risco,
                            response.body()!![index].incidencia_categoria,
                            response.body()!![index].incidencia.toString(),
                            response.body()!![index].population.toString(),
                            response.body()!![index].confirmados_14.toString(),

                            getComparativeFraction(
                                response.body()!![index].population,
                                response.body()!![index].confirmados_14
                            ),

                            response.body()!![index].data,
                        )
                    )
                    index++
                }

            }
            updateLocalData(repository.local, councils)

        }
        else{
            councils = getLocalData(repository.local) as ArrayList<County>
        }
        return councils
    }

    private fun getComparativeFraction(population: Int, confirmed: Int) : String {
        if(confirmed != 0){
            return (population/confirmed).toString()
        }
        return "0"
    }

    private fun updateLocalData(
        local: CouncilsDao, counties: ArrayList<County>) = runBlocking{
        local.insertCouncils(counties)
    }

    private fun getLocalData(local: CouncilsDao):List<County> = runBlocking{
        return@runBlocking local.getAllCouncils()
    }

    private fun getCounties(
        service: CountyDataService) : Response<List<CountyDataResponse>> = runBlocking{
        return@runBlocking service.getAllCountiesData()
    }

}