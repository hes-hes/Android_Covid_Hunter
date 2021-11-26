package pt.ulusofona.deisi.a2020.cm.g28.ui.viewmodels

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.deisi.a2020.cm.g28.data.remote.RetrofitBuilder
import pt.ulusofona.deisi.a2020.cm.g28.domain.logic.DegreeOfRiskLogic

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val ENDPOINT = "https://covid19-api.vost.pt/Requests/"

    private val remote = RetrofitBuilder.getInstance(ENDPOINT)

    fun getDegreeOfRisk(
        latitude : Double, longitude : Double, geocoder: Geocoder) : ArrayList<String> {
        val degreeOfRiskLogic = DegreeOfRiskLogic(remote)
        return degreeOfRiskLogic.getDegreeOfDanger(latitude, longitude, geocoder)
    }

}