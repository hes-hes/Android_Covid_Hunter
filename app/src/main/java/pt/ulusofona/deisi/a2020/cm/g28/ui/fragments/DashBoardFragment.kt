package pt.ulusofona.deisi.a2020.cm.g28.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_dash_board.*
import org.eazegraph.lib.models.PieModel
import pt.ulusofona.deisi.a2020.cm.g28.NavigationManager
import pt.ulusofona.deisi.a2020.cm.covidhunter.R
import pt.ulusofona.deisi.a2020.cm.g28.ui.viewmodels.DashBoardViewModel
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.BLUE_900
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.GREEN
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.RED
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_FRAGMENT
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Fragments
import java.lang.IndexOutOfBoundsException
import kotlin.collections.ArrayList

@SuppressLint("NonConstantResourceId")
class DashBoardFragment : Fragment() {

    private lateinit var viewModel: DashBoardViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_dash_board, container, false)
        ButterKnife.bind(this, view)
        viewModel = ViewModelProvider(this).get(DashBoardViewModel::class.java)

        return view
    }

    override fun onStart() {
        super.onStart()
        /*
            updatedData[0] = (newCases)
            updatedData[1] = (actives)
            updatedData[2] = (hospitalized)
            updatedData[3] = (deaths)
            updatedData[4] = (confirmed)
            updatedData[5] = (recovered)
            updatedData[6] = (dateTime)
            updatedData[7] = (critical_cases)
        */
        try {

            val updatedData: ArrayList<String> = viewModel.updatedData()

            val newCases = updatedData[0]
            val activeCases = updatedData[1]
            val hospitalizedCases = updatedData[2]
            val deathsCases = updatedData[3]
            val confirmedCases = updatedData[4]
            val recoveredCases = updatedData[5]
            val lastUpdate = updatedData[6]
            val critical = updatedData[7]

            new_cases.text = newCases
            active_cases.text = activeCases
            hospitalized.text = hospitalizedCases
            deaths.text = deathsCases
            confirmed.text = confirmedCases
            recovered.text = recoveredCases
            critical_cases.text = critical

            createPieChart(viewModel.getChartData(activeCases.toInt(), recoveredCases.toInt()))

            Toast.makeText(
                this.context, viewModel.getToastMessage(lastUpdate), Toast.LENGTH_LONG).show()

        }
        catch (ex: IndexOutOfBoundsException){

            Log.i("EXCEPTION", "NO DATA LOADED")

            new_cases.text = "-"
            active_cases.text = "-"
            hospitalized.text = "-"
            deaths.text = "-"
            confirmed.text = "-"
            recovered.text = "-"
            critical_cases.text = "-"

            val alertDialog = createNoDataDialog()
            alertDialog.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun createPieChart(chartData: ArrayList<Float>){
        /*
            chartData[0] = activeCases
            chartData[1] = population
            chartData[2] = recovered
        */
        val activeCasesChartPercentage = chartData[0]
        val populationChartPercentage = chartData[1]
        val recoveredChartPercentage = chartData[2]

        piechart.addPieSlice(
            PieModel(
                "population",
                populationChartPercentage,
                Color.parseColor(BLUE_900)
            )
        )

        piechart.addPieSlice(
            PieModel(
                "recovered",
                recoveredChartPercentage,
                Color.parseColor(GREEN)
            )
        )
        recovered_percentage.text = "${String.format("%.2f", recoveredChartPercentage)}%"

        piechart.addPieSlice(
            PieModel(
                "actives",
                chartData[0],
                Color.parseColor(RED)
            )
        )
        piechart.startAnimation()
        active_percentage.text = "${String.format("%.2f", activeCasesChartPercentage)}%"

    }

    private fun createNoDataDialog() : AlertDialog{

        val alertDialog: AlertDialog = AlertDialog.Builder(activity).create()

        val alertComponents = viewModel.getNoDataAlertComponents()

        alertDialog.setTitle(alertComponents["TITLE"])
        alertDialog.setMessage(alertComponents["MESSAGE"])

        alertDialog.setCancelable(false)
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "OK") { dialog, _ -> dialog.dismiss() }

        return alertDialog

    }


    //NAVIGATION
    @OnClick(R.id.button_contacts)
    fun onClickShowContacts(){
        activity?.let {
            CURRENT_FRAGMENT = Fragments.CONTACTS
            NavigationManager.changeFragment(it.supportFragmentManager)
        }
    }

    @OnClick(R.id.button_counties)
    fun onClickShowCounties(){
        activity?.let {
            CURRENT_FRAGMENT = Fragments.COUNTYLIST
            NavigationManager.changeFragment(it.supportFragmentManager)
        }
    }

    @OnClick(R.id.button_test_list)
    fun onClickShowTests(){
        activity?.let {
            CURRENT_FRAGMENT = Fragments.TESTLIST
            NavigationManager.changeFragment(it.supportFragmentManager)
        }
    }

    @OnClick(R.id.button_settings)
    fun onClickShowSettings(){
        activity?.let {
            CURRENT_FRAGMENT = Fragments.SETTINGS
            NavigationManager.changeFragment(it.supportFragmentManager)
        }
    }

}