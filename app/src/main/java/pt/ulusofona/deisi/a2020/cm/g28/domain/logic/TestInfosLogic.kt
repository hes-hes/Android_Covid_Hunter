package pt.ulusofona.deisi.a2020.cm.g28.domain.logic

import kotlinx.coroutines.runBlocking
import pt.ulusofona.deisi.a2020.cm.g28.data.local.room.dao.CovidTestDao
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_LANGUAGE
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Languages
import kotlin.collections.HashMap

class TestInfosLogic(private val storage: CovidTestDao) {

    fun getDeleteTestAlertComponents() : HashMap<String, String> {

        val components = HashMap<String,String>(3)

        if (CURRENT_LANGUAGE == Languages.ENGLISH){
            components["MESSAGE"] = "DO YOU REALLY WANT TO DELETE THE TEST?"
            components["NO_BUTTON"] = "NO"
            components["YES_BUTTON"] = "YES"
        }
        else{
            //else if (CURRENT_LANGUAGE == Languages.PORTUGUESE)
            components["MESSAGE"] = "DESEJA MESMO ELIMINAR O TESTE?"
            components["NO_BUTTON"] = "NÃO"
            components["YES_BUTTON"] = "SIM"
        }

        return components

    }

    fun deleteTest(uuid: String){
        deleteTestFromDB(storage, uuid)
    }

    private fun deleteTestFromDB(storage: CovidTestDao, uuid: String) = runBlocking{
        storage.deleteTest(uuid)
    }

}