package pt.ulusofona.deisi.a2020.cm.g28.domain.logic

import android.location.Geocoder
import kotlinx.coroutines.runBlocking
import pt.ulusofona.deisi.a2020.cm.g28.data.remote.responses.CountyDataResponse
import pt.ulusofona.deisi.a2020.cm.g28.data.remote.services.CountyDataService
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_LANGUAGE
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.INTERNET_CONNECTION
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Languages
import retrofit2.Response
import retrofit2.Retrofit
import java.util.ArrayList

class DegreeOfRiskLogic(val remote : Retrofit) {

    fun getDegreeOfDanger(
        latitude : Double, longitude : Double, geocoder: Geocoder) : ArrayList<String> {

        var unknownCountyMSG = ""
        var unknownRiskMSG = ""

        if(CURRENT_LANGUAGE == Languages.ENGLISH){
            unknownCountyMSG = "Unknown county"
            unknownRiskMSG = "Unknown danger"
        }
        else if(CURRENT_LANGUAGE == Languages.PORTUGUESE){
            unknownCountyMSG = "Concelho desconhecido"
            unknownRiskMSG = "Risco desconhecido"
        }

        val countyDataSet: ArrayList<String> = ArrayList()

        if(INTERNET_CONNECTION) {

            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses.size > 0) {

                val country = addresses[0].countryName
                val county = addresses[0].locality

                if (country != "Portugal") {

                    if(county == null){
                        countyDataSet.add(country)
                    }
                    else{
                        countyDataSet.add("$county, $country")
                    }
                    countyDataSet.add(unknownRiskMSG)

                    return countyDataSet
                }

                if (county != null) {

                    val service = remote.create(CountyDataService::class.java)
                    val response: Response<List<CountyDataResponse>> =
                        getCountyData(service, county)

                    if (response.isSuccessful){

                        val risk = response.body()?.get(0)?.incidencia_risco

                        if (risk != null) {
                            countyDataSet.add("$county, $country")
                            countyDataSet.add(risk)
                        }
                        else {
                            countyDataSet.add(unknownCountyMSG)
                            countyDataSet.add(unknownRiskMSG)
                        }

                        return countyDataSet

                    }

                }

            }
        }

        countyDataSet.add(unknownCountyMSG)
        countyDataSet.add(unknownRiskMSG)

        return countyDataSet

    }

    private fun getCountyData(service: CountyDataService, county: String)
    : Response<List<CountyDataResponse>> = runBlocking{
        return@runBlocking service.getCountyData(county)
    }

}