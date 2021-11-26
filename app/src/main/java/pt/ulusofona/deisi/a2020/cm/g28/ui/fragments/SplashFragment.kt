package pt.ulusofona.deisi.a2020.cm.g28.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pt.ulusofona.deisi.a2020.cm.g28.ui.activities.MainActivity
import pt.ulusofona.deisi.a2020.cm.covidhunter.R
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_FRAGMENT
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Fragments


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed(
            {
                CURRENT_FRAGMENT = Fragments.DASHBOARD
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            } , 3000)

    }
}