package pt.ulusofona.deisi.a2020.cm.g28.ui.fragments

import android.annotation.SuppressLint
import android.content.*
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_county_list.*
import kotlinx.android.synthetic.main.fragment_dash_board.*
import kotlinx.android.synthetic.main.fragment_test_list.*
import kotlinx.android.synthetic.main.fragment_test_list.view.*
import kotlinx.coroutines.*
import pt.ulusofona.deisi.a2020.cm.g28.NavigationManager
import pt.ulusofona.deisi.a2020.cm.covidhunter.R
import pt.ulusofona.deisi.a2020.cm.g28.ui.adapters.CouncilsAdapter
import pt.ulusofona.deisi.a2020.cm.g28.ui.viewmodels.CountyListViewModel
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_FRAGMENT
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.INTERNET_CONNECTION
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Settings.CLEAR_FILTER_SHAKING
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Fragments
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sqrt

@SuppressLint("NonConstantResourceId")
class CountyListFragment : Fragment(){

    private lateinit var viewModel: CountyListViewModel

    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    var adapter : CouncilsAdapter = CouncilsAdapter(ArrayList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_county_list, container, false)
        ButterKnife.bind(this, view)
        viewModel = ViewModelProvider(this).get(CountyListViewModel::class.java)
        
        return view
    }

    override fun onStart() {
        super.onStart()

        val countyRecyclerView: RecyclerView = this@CountyListFragment.county_recyclerview
        updateInternetConnection()
        val allCouncils = viewModel.getCountyList()
        
        countyRecyclerView.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        adapter = CouncilsAdapter(allCouncils)

        county_recyclerview.adapter = adapter
        
        val queryListener : SearchView.OnQueryTextListener = object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null) {
                    adapter.filter.filter(p0)
                }
                return false
            }
        }

        counties_search_filter.imeOptions = EditorInfo.IME_ACTION_DONE
        counties_search_filter.setOnQueryTextListener(queryListener)

        clearFilterSensor()

    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent) {

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration

            acceleration = acceleration * 0.9f + delta

            if (acceleration > 3 && CLEAR_FILTER_SHAKING) {
                counties_search_filter?.setQuery("", true)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    }

    private fun clearFilterSensor(){

        sensorManager = activity?.getSystemService(SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 1f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

    }

    @Suppress("DEPRECATION")
    fun updateInternetConnection(){
        val conManager = activity?.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo =conManager.activeNetworkInfo
        INTERNET_CONNECTION = internetInfo!=null && internetInfo.isConnected
    }

    //NAVIGATION
    @OnClick(R.id.button_contacts)
    fun onClickShowContacts(){
        activity?.let {
            CURRENT_FRAGMENT = Fragments.CONTACTS
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

    @OnClick(R.id.button_dashboard)
    fun onClickShowDashBoard(){
        activity?.let {
            CURRENT_FRAGMENT = Fragments.DASHBOARD
            NavigationManager.changeFragment(it.supportFragmentManager)
        }
    }


}