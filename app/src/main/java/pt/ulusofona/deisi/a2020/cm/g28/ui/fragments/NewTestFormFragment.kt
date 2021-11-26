package pt.ulusofona.deisi.a2020.cm.g28.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.*
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_new_test_form.*
import kotlinx.android.synthetic.main.fragment_new_test_form.view.*
import pt.ulusofona.deisi.a2020.cm.g28.NavigationManager
import pt.ulusofona.deisi.a2020.cm.covidhunter.R
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.TestCreationStatus
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals
import java.io.ByteArrayOutputStream
import java.util.*
import android.content.Intent
import pt.ulusofona.deisi.a2020.cm.g28.ui.viewmodels.NewTestViewModel
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_FRAGMENT
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Fragments

@SuppressLint("NonConstantResourceId")
class NewTestFormFragment : Fragment(), OnDateSetListener {

    private lateinit var viewModel: NewTestViewModel
    private lateinit var spinner: Spinner
    private val REQUEST_IMAGE_CAPTURE = 1

    var result : String = ""
    private var pic: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

    private val calendar: Calendar = Calendar.getInstance()
    private var day = calendar.get(Calendar.DAY_OF_MONTH)
    private var month = calendar.get(Calendar.MONTH)
    private var year = calendar.get(Calendar.YEAR)

    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0

    var resultOptions = arrayOf("")

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_new_test_form, container, false)
        viewModel = ViewModelProvider(this).get(NewTestViewModel::class.java)
        resultOptions = viewModel.parseResultOptions()
        ButterKnife.bind(this, view)
        spinner = view.result_spinner
        pickResult()

        return view
    }

    override fun onStart() {
        super.onStart()

        region_input_field.imeOptions = EditorInfo.IME_ACTION_DONE
        region_input_field.setSingleLine()
        date_input_field.setSingleLine()

        /*new_test_photo_btn.setOnClickListener {
            dispatchTakePictureIntent()
        }*/

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            Globals.COVID_TEST_PICTURE = imageBitmap
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        date_input_field.text.clear()
        date_input_field.text.append(
            viewModel.getDateAsString(savedDay, savedMonth + 1, savedYear)
        )

    }

    private fun pickResult(){

        spinner.adapter = activity?.let {
            ArrayAdapter(it, R.layout.result_dropdown_layout, resultOptions.toList())
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                result = resultOptions[position]
            }

        }

    }

    @OnClick(R.id.new_test_submit_btn)
    fun createTest(){

        val date = arrayOf(savedDay, savedMonth, savedYear)

        if (Globals.COVID_TEST_PICTURE != null){
            pic = Globals.COVID_TEST_PICTURE!!
            Globals.COVID_TEST_PICTURE = null
        }

        val stream = ByteArrayOutputStream()
        pic.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val picByteArray = stream.toByteArray()

        val status = viewModel.createTest(
            date,
            date_input_field.text.toString(),
            region_input_field.text.toString(),
            result,
            picByteArray
        )

        // - - - - - - - -

        if(status == TestCreationStatus.TEST_CREATED){
            activity?.let {
                CURRENT_FRAGMENT = Fragments.TESTLIST
                NavigationManager.changeFragment(it.supportFragmentManager)
            }
        }
        else{

            val errorMessage = viewModel.parseNewTestErrorMSG(status)

            val alertDialog: AlertDialog = AlertDialog.Builder(activity).create()
            alertDialog.setMessage(errorMessage)
            alertDialog.setCancelable(false)
            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE,
                "OK"
            ) { dialog, _ -> dialog.dismiss() }
            alertDialog.show()

        }

    }

    @OnClick(R.id.cancel_btn)
    fun onClickCancel(){
        activity?.let {
            Globals.COVID_TEST_PICTURE = null
            it.onBackPressed()
        }
    }

    @OnClick(R.id.date_input_field)
    fun pickDate(){
        activity?.let {
            DatePickerDialog(it, this, year, month, day).show()
        }
    }

    @OnClick(R.id.new_test_photo_btn)
    fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            //ERROR
        }
    }


}












