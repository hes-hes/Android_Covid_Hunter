package pt.ulusofona.deisi.a2020.cm.g28

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import pt.ulusofona.deisi.a2020.cm.covidhunter.R
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_FRAGMENT
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Fragments
import pt.ulusofona.deisi.a2020.cm.g28.ui.fragments.*

abstract class NavigationManager {

    companion object{

        private fun placeFragment(sfm: FragmentManager, f: Fragment){
            val transition = sfm.beginTransaction()
            transition.replace(R.id.mainFrame, f)
            transition.addToBackStack(null)
            transition.commit()
        }

        fun changeFragment(sfm: FragmentManager){

            when (CURRENT_FRAGMENT) {

                Fragments.DASHBOARD -> {
                    placeFragment(sfm, DashBoardFragment())
                }
                Fragments.SETTINGS -> {
                    placeFragment(sfm, SettingsFragment())
                }
                Fragments.TESTLIST -> {
                    placeFragment(sfm, TestListFragment())
                }
                Fragments.COUNTYLIST -> {
                    placeFragment(sfm, CountyListFragment())
                }
                Fragments.CONTACTS -> {
                    placeFragment(sfm, ContactsFragment())
                }
                Fragments.TESTINFO -> {
                    placeFragment(sfm, TestInfosFragment())
                }
                Fragments.NEWTESTFORM -> {
                    placeFragment(sfm, NewTestFormFragment())
                }
                else -> {
                    placeFragment(sfm, DashBoardFragment())
                }

            }
        }

    }
}