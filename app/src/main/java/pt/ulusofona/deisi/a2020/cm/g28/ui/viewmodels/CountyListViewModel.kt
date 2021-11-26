package pt.ulusofona.deisi.a2020.cm.g28.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.deisi.a2020.cm.g28.data.local.room.CountyDatabase
import pt.ulusofona.deisi.a2020.cm.g28.data.remote.RetrofitBuilder
import pt.ulusofona.deisi.a2020.cm.g28.data.repositories.CouncilsRepository
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.County
import pt.ulusofona.deisi.a2020.cm.g28.domain.logic.CountyListLogic

class CountyListViewModel(app: Application) : AndroidViewModel(app) {

    private val ENDPOINT = "https://covid19-api.vost.pt/Requests/"

    private val storage = CountyDatabase.getInstance(app).councilsDao()
    private val remote = RetrofitBuilder.getInstance(ENDPOINT)
    private val repository = CouncilsRepository(storage, remote)

    private val countyListLogic = CountyListLogic(repository)

    fun getCountyList(): List<County> {
        return countyListLogic.getCounties()
    }

}