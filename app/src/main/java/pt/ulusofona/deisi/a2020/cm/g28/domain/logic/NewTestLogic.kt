package pt.ulusofona.deisi.a2020.cm.g28.domain.logic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.a2020.cm.g28.data.local.room.dao.CovidTestDao
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.CovidTest
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_LANGUAGE
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.TestCreationStatus
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Languages
import java.util.*

class NewTestLogic(private val storage: CovidTestDao) {

    fun createTest(date: Array<Int>, dateString: String,
                   local: String, result: String, photo: ByteArray) : TestCreationStatus {

        val day = date[0]
        val month = date[1]
        val year = date[2]

        if(day == 0 || month == 0 || year == 0) {
            return TestCreationStatus.NO_DATE
        }

        val cal: Calendar = Calendar.getInstance()
        if(
            (month > cal.get(Calendar.MONTH) && year == cal.get(Calendar.YEAR)) ||
            (year > cal.get(Calendar.YEAR)) ||
            (year == cal.get(Calendar.YEAR) && month == cal.get(Calendar.MONTH)
                    && day > cal.get(Calendar.DAY_OF_MONTH))){
            return TestCreationStatus.INVALID_DATE
        }

        if(local == "") {
            return TestCreationStatus.NO_LOCATION
        }

        if(result == "Indica o resultado" || result == "Indicate the result") {
            return TestCreationStatus.NO_RESULT
        }

        CoroutineScope(Dispatchers.IO).launch {
            storage.insertTest(
                CovidTest(dateString, local, result, photo)
            )
        }

        return TestCreationStatus.TEST_CREATED
    }

    fun getDateAsString(savedDay : Int, savedMonth : Int, savedYear : Int) : String{

        val day: String
        val month: String

        if(savedDay < 10){
            day = "0$savedDay"
        }
        else{
            day = savedDay.toString()
        }
        if(savedMonth < 10){
            month = "0$savedMonth"
        }
        else{
            month = savedMonth.toString()
        }

        return "$day/$month/${savedYear}"

    }

    fun parseNewTestErrorMSG(status : TestCreationStatus): String {

        if(CURRENT_LANGUAGE == Languages.ENGLISH){
            when (status) {
                TestCreationStatus.NO_DATE -> {
                    return "The test date must be indicated"
                }
                TestCreationStatus.INVALID_DATE -> {
                    return "The date indicated is not valid"
                }
                TestCreationStatus.NO_LOCATION -> {
                    return "The test location must be indicated"
                }
                else -> {
                    return "The test result must be indicated"
                }
            }
        }
        else {
            //else if(CURRENT_LANGUAGE == Languages.PORTUGUESE)
            when (status) {
                TestCreationStatus.NO_DATE -> {
                    return "A data do teste tem que ser indicada"
                }
                TestCreationStatus.INVALID_DATE -> {
                    return "A data indicada não é válida"
                }
                TestCreationStatus.NO_LOCATION -> {
                    return "O local do teste tem que ser indicado"
                }
                else -> {
                    return "O resultado do teste tem que ser indicado"
                }
            }
        }

    }

    fun createResultOptions() : Array<String> {

        if(CURRENT_LANGUAGE == Languages.ENGLISH){
            return arrayOf("Indicate the test result","NEGATIVE", "POSITIVE", "INCONCLUSIVE")
        }
        else {
            //else if(CURRENT_LANGUAGE == Languages.PORTUGUESE)
            return arrayOf("Indica o resultado do teste","NEGATIVO", "POSITIVO", "INCONCLUSIVO")
        }

    }

}