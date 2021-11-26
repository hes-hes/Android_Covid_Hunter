package pt.ulusofona.deisi.a2020.cm.g28.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.deisi.a2020.cm.g28.data.local.room.CovidTestDatabase
import pt.ulusofona.deisi.a2020.cm.g28.domain.logic.TestInfosLogic

class TestInfosViewModel(app: Application) : AndroidViewModel(app) {

    private val testStorage = CovidTestDatabase.getInstance(app).covidTestDao()
    private val testInfosLogic = TestInfosLogic(testStorage)

    fun deleteTest(uuid: String){
        testInfosLogic.deleteTest(uuid)
    }

    fun getDeleteTestAlertComponents() : HashMap<String, String>{
        return testInfosLogic.getDeleteTestAlertComponents()
    }

}