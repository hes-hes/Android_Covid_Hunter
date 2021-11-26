package pt.ulusofona.deisi.a2020.cm.g28.ui.adapters

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.test_item_layout.view.*
import pt.ulusofona.deisi.a2020.cm.g28.NavigationManager
import pt.ulusofona.deisi.a2020.cm.covidhunter.R
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.CovidTest
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_FRAGMENT
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_LANGUAGE
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.GREEN
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.MAIN_FRAGMENT_MANAGER
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.RED
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.TEST_TO_SHOW_INFOS
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Fragments
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Languages

class CovidTestAdapter(val testList: List<CovidTest>)
    : RecyclerView.Adapter<CovidTestAdapter.TestViewHolder>(),
    Filterable {

    //ITEM_VIEW
    class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textResult: TextView = itemView.test_item_result
        val testDate: TextView = itemView.test_item_date
        val infoButton: ImageButton = itemView.info_icon_btn
        val photo : ImageView = itemView.test_photo
    }

    var copyOfTestList : ArrayList<CovidTest> = ArrayList(testList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val testItemView = LayoutInflater.from(parent.context).
        inflate(R.layout.test_item_layout, parent, false)
        return TestViewHolder(testItemView)
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {

        val currentTestItem: CovidTest = copyOfTestList[position]

        holder.testDate.text = currentTestItem.date
        holder.textResult.text = parseResult(currentTestItem.result)

        holder.photo.setImageBitmap(
            BitmapFactory.decodeByteArray(
                currentTestItem.photo,
                0,
                currentTestItem.photo.size
            )
        )

        holder.textResult.setTextColor(parseResultColor(holder.textResult.text.toString()))

        holder.infoButton.setOnClickListener {
            TEST_TO_SHOW_INFOS = currentTestItem
            CURRENT_FRAGMENT = Fragments.TESTINFO
            NavigationManager.changeFragment(MAIN_FRAGMENT_MANAGER)
        }

    }

    override fun getItemCount() = copyOfTestList.size

    override fun getFilter(): Filter {

        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filteredList = ArrayList<CovidTest>()

                if(constraint == null || constraint.isEmpty()){
                    filteredList.addAll(testList)
                }
                else{
                    val filterPattern : String = constraint.toString().lowercase().trim()

                    for (test in testList){
                        if (test.date.lowercase().contains(filterPattern) ||
                            test.local.lowercase().contains(filterPattern)||
                            test.result.lowercase().contains(filterPattern)
                        ){
                            filteredList.add(test)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                copyOfTestList.clear()
                copyOfTestList.addAll(filterResults.values as ArrayList<CovidTest>)
                notifyDataSetChanged()
            }
        }
    }

    private fun parseResultColor(result : String) : Int{
        if (result == "NEGATIVO" || result == "NEGATIVE") {
            return Color.parseColor(GREEN)
        }
        if (result == "POSITIVO" || result == "POSITIVE") {
            return Color.parseColor(RED)
        }
        return Color.parseColor(Globals.BLUE_900)
    }

    private fun parseResult(result : String) : String{

        if(CURRENT_LANGUAGE == Languages.ENGLISH){

            if(result == "POSITIVE" ||
               result == "NEGATIVE" ||
               result == "INCONCLUSIVE"
            ){
                return result
            }

            when (result) {
                "POSITIVO" -> {
                    return "POSITIVE"
                }
                "NEGATIVO" -> {
                    return "NEGATIVE"
                }
                "INCONCLUSIVO" -> {
                    return "INCONCLUSIVE"
                }
            }

        }
        else if(CURRENT_LANGUAGE == Languages.PORTUGUESE){

            if(result == "POSITIVO" ||
                result == "NEGATIVO" ||
                result == "INCONCLUSIVO"
            ){
                return result
            }

            when (result) {
                "POSITIVE" -> {
                    return "POSITIVO"
                }
                "NEGATIVE" -> {
                    return "NEGATIVO"
                }
                "INCONCLUSIVE" -> {
                    return "INCONCLUSIVO"
                }
            }

        }

        return ""

    }
}
