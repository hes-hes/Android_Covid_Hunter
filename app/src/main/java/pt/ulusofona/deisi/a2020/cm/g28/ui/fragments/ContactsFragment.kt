package pt.ulusofona.deisi.a2020.cm.g28.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import pt.ulusofona.deisi.a2020.cm.g28.NavigationManager
import pt.ulusofona.deisi.a2020.cm.covidhunter.R
import android.content.Intent
import android.net.Uri
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_FRAGMENT
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Fragments

@SuppressLint("NonConstantResourceId")
class ContactsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_contacts, container, false)
        ButterKnife.bind(this, view)

        return view
    }

    //CALL INTENTS
    @OnClick(R.id.social_line_btn)
    fun callSocialSecurityLine(){
        val phone = "+351300502502"
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
        startActivity(intent)
    }

    @OnClick(R.id.sns_line_btn)
    fun callSNSLine(){
        val phone = "+351808242424"
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
        startActivity(intent)
    }

    @OnClick(R.id.consular_line_btn)
    fun callConsularOfficeLine(){
        val phone = "+351961706472"
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
        startActivity(intent)
    }


    //NAVIGATION
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

    @OnClick(R.id.button_dashboard)
    fun onClickShowDashBoard(){
        activity?.let {
            CURRENT_FRAGMENT = Fragments.DASHBOARD
            NavigationManager.changeFragment(it.supportFragmentManager)
        }
    }

}