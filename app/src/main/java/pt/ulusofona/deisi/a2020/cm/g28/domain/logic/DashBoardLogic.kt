package pt.ulusofona.deisi.a2020.cm.g28.domain.logic

import android.util.Log
import kotlinx.coroutines.runBlocking
import pt.ulusofona.deisi.a2020.cm.g28.data.local.room.dao.DashBoardDao
import pt.ulusofona.deisi.a2020.cm.g28.data.remote.responses.DashBoardDataResponse
import pt.ulusofona.deisi.a2020.cm.g28.data.remote.services.DashBoardDataService
import pt.ulusofona.deisi.a2020.cm.g28.data.repositories.DashBoardRepository
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.DashBoardData
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_LANGUAGE
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Languages
import retrofit2.Response
import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.collections.HashMap

class DashBoardLogic(private val dashBoardRepository: DashBoardRepository) {

    fun updatedData() : ArrayList<String> {

        val dataSet: ArrayList<String> = ArrayList()
        val service = dashBoardRepository.remote.create(DashBoardDataService::class.java)

        if(Globals.INTERNET_CONNECTION){

            val response: Response<DashBoardDataResponse> = getRemoteData(service)

            val cal = Calendar.getInstance()
            val dateTimeParts = cal.time.toString().split(" ")
            val dateTime = dateTimeParts[2] + " " +
                           dateTimeParts[1] + " " +
                           dateTimeParts[5] + " - " +
                           dateTimeParts[3]

            if(response.isSuccessful){

                val dashBoardDataNew = DashBoardData("","","","","","","")

                val newCases = response.body()?.confirmados_novos.toString()
                val actives = response.body()?.ativos.toString()
                val hospitalized = response.body()?.internados.toString()
                val deaths = response.body()?.obitos.toString()
                val confirmed = response.body()?.confirmados.toString()
                val recovered = response.body()?.recuperados.toString()
                val critical = response.body()?.internados_uci.toString()

                dataSet.add(newCases)
                dataSet.add(actives)
                dataSet.add(hospitalized)
                dataSet.add(deaths)
                dataSet.add(confirmed)
                dataSet.add(recovered)
                dataSet.add(dateTime)
                dataSet.add(critical)

                dashBoardDataNew.newCases = newCases
                dashBoardDataNew.activeCases = actives
                dashBoardDataNew.hospitalized = hospitalized
                dashBoardDataNew.deaths = deaths
                dashBoardDataNew.confirmedCases = confirmed
                dashBoardDataNew.recovered = recovered
                dashBoardDataNew.dateTime = dateTime
                dashBoardDataNew.criticals = critical

                updateLocalData(dashBoardRepository.local, dashBoardDataNew)

            }

        }
        else{

            val dashBoardDataFromDB = getLocalData(dashBoardRepository.local)

            try{
                dataSet.add(dashBoardDataFromDB[0].newCases)
                dataSet.add(dashBoardDataFromDB[0].activeCases)
                dataSet.add(dashBoardDataFromDB[0].hospitalized)
                dataSet.add(dashBoardDataFromDB[0].deaths)
                dataSet.add(dashBoardDataFromDB[0].confirmedCases)
                dataSet.add(dashBoardDataFromDB[0].recovered)
                dataSet.add(dashBoardDataFromDB[0].dateTime)
                dataSet.add(dashBoardDataFromDB[0].criticals)
            }
            catch (ex: IndexOutOfBoundsException){
                //Log.i("EXCEPTION", "THERE IS NO DATA ON DATABASE")
            }

        }

        return dataSet

    }

    fun getPieChartData(activeCases: Int, recovered: Int) : ArrayList<Float>{

        val population = 10138633

        val dataList: ArrayList<Float> = ArrayList()

        val activesPercentage = (activeCases.toFloat() * 100) / population.toFloat()
        val recoveredPercentage = (recovered.toFloat() * 100) / population.toFloat()
        val populationPercentage = 100 - (activesPercentage + recoveredPercentage)

        /*
          dataList[0] = activeCases
          dataList[1] = population
          dataList[2] = recovered
        */

        dataList.add(activesPercentage)
        dataList.add(populationPercentage)
        dataList.add(recoveredPercentage)

        return dataList

    }

    fun getNoDataAlertComponents() : HashMap<String, String>{

        val components = HashMap<String, String>(2)

        if(CURRENT_LANGUAGE == Languages.ENGLISH){
            components["TITLE"] = "UNABLE TO UPDATE DATA!"
            components["MESSAGE"] = "Check your internet connection."
        }
        else{
            //else if(CURRENT_LANGUAGE == Languages.PORTUGESE){
            components["TITLE"] = "NÃO FOI POSSIVEL ATUALIZAR OS DADOS!"
            components["MESSAGE"] = "Verifique a sua ligação à internet."
        }

        return components

    }

    fun getToastMessage(lastUpdate : String) : String{
        if(CURRENT_LANGUAGE == Languages.ENGLISH){
            return "Last update - $lastUpdate"
        }
        else{
            //if(CURRENT_LANGUAGE == Languages.PORTUGUESE)
            return "Última atualização - $lastUpdate"
        }
    }

    private fun getRemoteData(
        service: DashBoardDataService):Response<DashBoardDataResponse> = runBlocking{
        return@runBlocking service.getUpdatedData()
    }

    private fun getLocalData(
        local: DashBoardDao):List<DashBoardData> = runBlocking{
        return@runBlocking local.getAllDashBoardData()
    }

    private fun updateLocalData(
        local: DashBoardDao, dashBoardData: DashBoardData) = runBlocking{
        local.updateDashBoard(dashBoardData)
    }

}
