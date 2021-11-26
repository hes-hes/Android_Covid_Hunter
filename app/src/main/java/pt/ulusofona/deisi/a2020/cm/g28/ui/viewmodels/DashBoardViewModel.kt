package pt.ulusofona.deisi.a2020.cm.g28.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.deisi.a2020.cm.g28.data.local.room.DashBoardDatabase
import pt.ulusofona.deisi.a2020.cm.g28.data.remote.RetrofitBuilder
import pt.ulusofona.deisi.a2020.cm.g28.data.repositories.DashBoardRepository
import pt.ulusofona.deisi.a2020.cm.g28.domain.logic.DashBoardLogic

class DashBoardViewModel(app: Application) : AndroidViewModel(app) {

    private val ENDPOINT = "https://covid19-api.vost.pt/Requests/"

    private val remote = RetrofitBuilder.getInstance(ENDPOINT)
    private val local = DashBoardDatabase.getInstance(app).dashBoardDao()
    private val repository = DashBoardRepository(local, remote)

    private val dashBoardLogic = DashBoardLogic(repository)

    fun updatedData() : ArrayList<String> {
        return dashBoardLogic.updatedData()
    }

    fun getToastMessage(lastUpdate : String) : String {
        return dashBoardLogic.getToastMessage(lastUpdate)
    }

    fun getChartData(activeCases : Int, recovered: Int) : ArrayList<Float>{
        return dashBoardLogic.getPieChartData(activeCases, recovered)
    }

    fun getNoDataAlertComponents() : HashMap<String, String>{
        return dashBoardLogic.getNoDataAlertComponents()
    }


}