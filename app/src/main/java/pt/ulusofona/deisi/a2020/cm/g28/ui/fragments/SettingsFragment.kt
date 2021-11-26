package pt.ulusofona.deisi.a2020.cm.g28.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_settings.*
import pt.ulusofona.deisi.a2020.cm.g28.NavigationManager
import pt.ulusofona.deisi.a2020.cm.covidhunter.R
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.BATTERY_PERCENTAGE
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_FRAGMENT
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Settings
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.DarkModeStatus
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Fragments

@SuppressLint("NonConstantResourceId")
class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        ButterKnife.bind(this, view)

        return view
    }

    override fun onStart() {
        super.onStart()

        updateSettings()

        darkmode_switch_enable.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Settings.DARK_MODE = DarkModeStatus.ENABLE
                CURRENT_FRAGMENT = Fragments.SETTINGS
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else{
                updateSettings()
            }
        }

        darkmode_switch_automatic.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Settings.DARK_MODE = DarkModeStatus.AUTOMATIC
                if(BATTERY_PERCENTAGE <= 20){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                updateSettings()
            }
            else{
                updateSettings()
            }
        }

        darkmode_switch_disable.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Settings.DARK_MODE = DarkModeStatus.DISABLE
                CURRENT_FRAGMENT = Fragments.SETTINGS
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                updateSettings()
            }
            else{
                updateSettings()
            }
        }

        shaking_switch.setOnCheckedChangeListener { _, isChecked ->
            Settings.CLEAR_FILTER_SHAKING = isChecked
        }

    }

    private fun updateSettings(){

        when (Settings.DARK_MODE) {

            DarkModeStatus.ENABLE -> {
                darkmode_switch_enable.isChecked = true
                darkmode_switch_disable.isChecked = false
                darkmode_switch_automatic.isChecked = false
            }
            DarkModeStatus.DISABLE -> {
                darkmode_switch_enable.isChecked = false
                darkmode_switch_disable.isChecked = true
                darkmode_switch_automatic.isChecked = false
            }
            DarkModeStatus.AUTOMATIC -> {
                darkmode_switch_enable.isChecked = false
                darkmode_switch_disable.isChecked = false
                darkmode_switch_automatic.isChecked = true
            }

        }

        shaking_switch.isChecked = Settings.CLEAR_FILTER_SHAKING

    }

    //Navigation
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

    @OnClick(R.id.button_dashboard)
    fun onClickShowDashBoard(){
        activity?.let {
            CURRENT_FRAGMENT = Fragments.DASHBOARD
            NavigationManager.changeFragment(it.supportFragmentManager)
        }
    }

}

