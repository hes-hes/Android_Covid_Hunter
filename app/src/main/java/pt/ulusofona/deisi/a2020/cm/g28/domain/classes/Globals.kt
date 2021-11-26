package pt.ulusofona.deisi.a2020.cm.g28.domain.classes

import android.graphics.Bitmap
import androidx.fragment.app.FragmentManager
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Fragments
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Languages

object Globals {

    var CURRENT_FRAGMENT = Fragments.SPLASH
    var CURRENT_LANGUAGE = Languages.ENGLISH
    var INTERNET_CONNECTION : Boolean = false

    lateinit var MAIN_FRAGMENT_MANAGER : FragmentManager
    lateinit var TEST_TO_SHOW_INFOS : CovidTest

    var BATTERY_PERCENTAGE : Float = 0.0F

    var COVID_TEST_PICTURE : Bitmap? = null

    //COLORS
    const val GREEN = "#037D50"
    const val YELLOW = "#ECB430"
    const val LIGHT_ORANGE = "#FF6A33"
    const val DARK_ORANGE = "#FF4500"
    const val RED = "#FF0000"
    const val BLUE_900 = "#0D47A1"

}