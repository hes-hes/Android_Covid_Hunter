package pt.ulusofona.deisi.a2020.cm.g28.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.deisi.a2020.cm.g28.data.local.room.CovidTestDatabase
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.CovidTest
import pt.ulusofona.deisi.a2020.cm.g28.domain.logic.TestListLogic

class TestListViewModel(app: Application) : AndroidViewModel(app) {

    private val testStorage = CovidTestDatabase.getInstance(app).covidTestDao()

    private val testListLogic = TestListLogic(testStorage)

    fun getTestList(): List<CovidTest> {
        return testListLogic.getAllList()
    }

}