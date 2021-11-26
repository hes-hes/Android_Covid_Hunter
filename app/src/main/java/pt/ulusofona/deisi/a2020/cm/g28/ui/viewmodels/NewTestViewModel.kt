package pt.ulusofona.deisi.a2020.cm.g28.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.deisi.a2020.cm.g28.data.local.room.CovidTestDatabase
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.TestCreationStatus
import pt.ulusofona.deisi.a2020.cm.g28.domain.logic.NewTestLogic

class NewTestViewModel(app: Application) : AndroidViewModel(app) {

    private val testStorage = CovidTestDatabase.getInstance(app).covidTestDao()
    private val newTestLogic = NewTestLogic(testStorage)

    fun createTest(date: Array<Int>, dateString: String,
                   local:String, result:String, photo:ByteArray) : TestCreationStatus {
        return newTestLogic.createTest(date, dateString, local, result, photo)
    }

    fun getDateAsString(savedDay : Int, savedMonth : Int, savedYear : Int) : String{
        return newTestLogic.getDateAsString(savedDay, savedMonth, savedYear)
    }

    fun parseNewTestErrorMSG(status : TestCreationStatus) : String {
        return newTestLogic.parseNewTestErrorMSG(status)
    }

    fun parseResultOptions() : Array<String>{
        return newTestLogic.createResultOptions()
    }

}