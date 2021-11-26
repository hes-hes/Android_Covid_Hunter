package pt.ulusofona.deisi.a2020.cm.g28.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dash_board.*
import pt.ulusofona.deisi.a2020.cm.g28.NavigationManager
import pt.ulusofona.deisi.a2020.cm.covidhunter.R
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Settings
import pt.ulusofona.deisi.a2020.cm.g28.ui.viewmodels.MainViewModel
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.BATTERY_PERCENTAGE
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_FRAGMENT
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_LANGUAGE
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.DARK_ORANGE
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.GREEN
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.LIGHT_ORANGE
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.MAIN_FRAGMENT_MANAGER
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.RED
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.YELLOW
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.DarkModeStatus
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Fragments
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Languages
import pt.ulusofona.deisi.a2020.cm.g28.ui.fragments.SplashFragment
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val REQUESTCODE = 100
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(CURRENT_FRAGMENT == Fragments.SPLASH){
            main_linear_layout?.visibility = LinearLayout.GONE

            val transition = supportFragmentManager.beginTransaction()
            updateInternetConnection()
            transition.replace(R.id.mainFrame, SplashFragment())
            transition.commit()
        }
        else{
            NavigationManager.changeFragment(supportFragmentManager)
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()

        getLocation()
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        MAIN_FRAGMENT_MANAGER = supportFragmentManager
        checkLanguage()

    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        if (count != 0) {
            if(CURRENT_FRAGMENT == Fragments.NEWTESTFORM ||
                CURRENT_FRAGMENT == Fragments.TESTINFO
            )
            {
                CURRENT_FRAGMENT = Fragments.TESTLIST
                supportFragmentManager.popBackStack()
            }
        }

    }

    @Suppress("DEPRECATION")
    fun updateInternetConnection(){
        val conManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo =conManager.activeNetworkInfo
        Globals.INTERNET_CONNECTION = internetInfo!=null && internetInfo.isConnected
    }

    private fun checkLanguage(){

        when {
            danger_indicator.text.toString() == "AM I IN DANGER?" -> {
                CURRENT_LANGUAGE = Languages.ENGLISH
            }
            danger_indicator.text.toString() == "ESTOU EM PERIGO?" -> {
                CURRENT_LANGUAGE = Languages.PORTUGUESE
            }
        }

    }

//  GEOLOCATION SENSOR *****************************************************************************

    @SuppressLint("MissingPermission")
    fun getLocation(){
        //PERMISSIONS
        if (permissionsNotChecked()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUESTCODE
            )
        }
        else {
            val listener = object : LocationListener {

                override fun onLocationChanged(p0: Location) {

                    //val geocoder = Geocoder(this@MainActivity, Locale.getDefault())

                    updateInternetConnection()

                    val degreeOfRisk =
                        viewModel.getDegreeOfRisk(
                            p0.latitude,
                            p0.longitude,
                            Geocoder(this@MainActivity, Locale.getDefault())
                        )

                    parseDegreeOfRisk(degreeOfRisk)

                }

                override fun onProviderEnabled(provider: String) {}

                override fun onProviderDisabled(provider: String) {}

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            }

            val manager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            manager.requestLocationUpdates(
                // x * 1000L = x seconds
                LocationManager.GPS_PROVIDER, 5000L, 500f, listener
            )

        }
    }

    private fun permissionsNotChecked() : Boolean {
        return(
            ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        &&
            ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        )
    }

    private fun parseDegreeOfRisk(degreeOfRisk : ArrayList<String>){
        /*
            Degree of risk -> index = 1
            Actual county -> index = 0
        */

        current_county?.text = degreeOfRisk[0]
        degree_of_risk?.text = degreeOfRisk[1]

        when {
            degreeOfRisk[1] == "Baixo a Moderado" -> {
                degree_of_risk?.setTextColor(Color.parseColor(GREEN))
            }
            degreeOfRisk[1] == "Moderado" -> {
                degree_of_risk?.setTextColor(Color.parseColor(YELLOW))
            }
            degreeOfRisk[1] == "Elevado" -> {
                degree_of_risk?.setTextColor(Color.parseColor(LIGHT_ORANGE))
            }
            degreeOfRisk[1] == "Muito Elevado" -> {
                degree_of_risk?.setTextColor(Color.parseColor(DARK_ORANGE))
            }
            degreeOfRisk[1] == "Extremamente Elevado" -> {
                degree_of_risk?.setTextColor(Color.parseColor(RED))
            }
            else -> {
                //DO NOTHING
            }
        }

    }

//**************************************************************************************************

//  BATTERY SENSOR *********************************************************************************

    private val batteryReceiver: BroadcastReceiver = object: BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {

            val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, 0)

            val percentage = level!! * 100 / scale!!.toFloat()
            BATTERY_PERCENTAGE = percentage

            if(Settings.DARK_MODE == DarkModeStatus.AUTOMATIC){
                if(percentage <= 20){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

        }

    }

//**************************************************************************************************

}