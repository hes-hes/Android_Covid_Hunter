package pt.ulusofona.deisi.a2020.cm.g28.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_test_infos.*
import pt.ulusofona.deisi.a2020.cm.covidhunter.R
import pt.ulusofona.deisi.a2020.cm.g28.ui.viewmodels.TestInfosViewModel
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.CovidTest
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.TEST_TO_SHOW_INFOS

@SuppressLint("NonConstantResourceId")

class TestInfosFragment : Fragment() {

    private lateinit var viewModel: TestInfosViewModel
    private lateinit var testToShow : CovidTest

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_test_infos, container, false)
        ButterKnife.bind(this, view)
        viewModel = ViewModelProvider(this).get(TestInfosViewModel::class.java)

        return view

    }

    override fun onStart() {
        super.onStart()

        testToShow = TEST_TO_SHOW_INFOS

        test_date.text = testToShow.date
        test_local.text = testToShow.local
        test_result.text = testToShow.result

        val photo = BitmapFactory.decodeByteArray(testToShow.photo, 0, testToShow.photo.size)
        test_photo.setImageBitmap(photo)

    }


    @OnClick(R.id.back_button)
    fun onClickGoBack(){
        activity?.onBackPressed()
    }

    @OnClick(R.id.delete_button)
    fun onClickDeleteTest(){

        val deleteAlert: AlertDialog = AlertDialog.Builder(activity).create()

        val alertComponents = viewModel.getDeleteTestAlertComponents()

        deleteAlert.setTitle(alertComponents["MESSAGE"])
        deleteAlert.setButton(AlertDialog.BUTTON_NEGATIVE, alertComponents["NO_BUTTON"]){ _, _ ->
            deleteAlert.dismiss()
        }
        deleteAlert.setButton(AlertDialog.BUTTON_POSITIVE, alertComponents["YES_BUTTON"]){ _, _ ->
            viewModel.deleteTest(testToShow.uuid)
            deleteAlert.dismiss()
            onClickGoBack()
        }
        deleteAlert.show()

    }

}