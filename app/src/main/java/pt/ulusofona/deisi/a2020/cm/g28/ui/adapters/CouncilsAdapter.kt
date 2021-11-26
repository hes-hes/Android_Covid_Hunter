package pt.ulusofona.deisi.a2020.cm.g28.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.county_item_layout.view.*
import pt.ulusofona.deisi.a2020.cm.covidhunter.R
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.County
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.CURRENT_LANGUAGE
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.BLUE_900
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.DARK_ORANGE
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.GREEN
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.LIGHT_ORANGE
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.RED
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.Globals.YELLOW
import pt.ulusofona.deisi.a2020.cm.g28.domain.enums.Languages
import kotlin.collections.ArrayList

class CouncilsAdapter(val countyList: List<County>) :
    RecyclerView.Adapter<CouncilsAdapter.CountyViewHolder>(),
    Filterable {

    //ITEM_VIEW
    class CountyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val countyName : TextView = itemView.county_name
        val districtName : TextView = itemView.district_name
        val riskDegree : TextView = itemView.risk_degree
        val incidence : TextView = itemView.incidence
        val incidenceInterval : TextView = itemView.incidence_interval

        val population : TextView? = itemView.population
        val confirmedCases : TextView? = itemView.confirmed_cases
        val comparativeFraction : TextView? = itemView.comparative_fraction

        val lastUpdate : TextView = itemView.last_update

    }

    var copyOfCountyList : ArrayList<County> = ArrayList(countyList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountyViewHolder {
        val countyItemView = LayoutInflater.from(parent.context).
        inflate(R.layout.county_item_layout, parent, false)
        return CountyViewHolder(countyItemView)
    }

    @SuppressLint("NewApi", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: CountyViewHolder, position: Int) {

        val currentCounty : County = copyOfCountyList[position]

        holder.countyName.text = currentCounty.name
        holder.districtName.text = currentCounty.district

        holder.riskDegree.text = currentCounty.riskDegree
        holder.riskDegree.setTextColor(parseDegreeOfRisk(currentCounty.riskDegree))
        holder.riskDegree.setTypeface(null, Typeface.BOLD)

        holder.incidence.text = currentCounty.incidence
        holder.incidenceInterval.text = currentCounty.incidenceInterval
        holder.lastUpdate.text = currentCounty.lastUpdate

        if(holder.population != null){
            holder.population.text = currentCounty.population
        }
        if(holder.confirmedCases != null){
            holder.confirmedCases.text = currentCounty.confirmedCases
        }
        if(holder.comparativeFraction != null){
            val fraction = currentCounty.comparativeFraction
            holder.comparativeFraction.text = parseComparativeFraction(fraction)
        }

    }

    override fun getItemCount() = copyOfCountyList.size

    //FILTER
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<County>()

                if(constraint == null || constraint.isEmpty()){
                    filteredList.addAll(countyList)
                }
                else{
                    val filterPattern : String = constraint.toString().lowercase().trim()

                    for (county in countyList){

                        if (county.name.lowercase().contains(filterPattern) ||
                            county.district.lowercase().contains(filterPattern) ||
                            county.incidence.lowercase().contains(filterPattern) ||
                            county.incidenceInterval.lowercase().contains(filterPattern) ||
                            county.lastUpdate.lowercase().contains(filterPattern) ||
                            county.riskDegree.lowercase().contains(filterPattern)
                        ) {
                            filteredList.add(county)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                copyOfCountyList.clear()
                copyOfCountyList.addAll(filterResults.values as ArrayList<County>)
                notifyDataSetChanged()
            }
        }
    }

    private fun parseDegreeOfRisk(degreeOfRisk : String) : Int{

        when (degreeOfRisk) {
            "Baixo a Moderado" -> {
                return Color.parseColor(GREEN)
            }
            "Moderado" -> {
                return Color.parseColor(YELLOW)
            }
            "Elevado" -> {
                return Color.parseColor(LIGHT_ORANGE)
            }
            "Muito Elevado" -> {
                return Color.parseColor(DARK_ORANGE)
            }
            "Extremamente Elevado" -> {
                return Color.parseColor(RED)
            }
            else -> return Color.parseColor(BLUE_900)
        }

    }

    private fun parseComparativeFraction(comparativeFraction: String) : String{
        if(CURRENT_LANGUAGE == Languages.ENGLISH){
            return "[1 in $comparativeFraction inhabitants]"
        }
        else if(CURRENT_LANGUAGE == Languages.PORTUGUESE){
            return "[1 em cada $comparativeFraction habitantes]"
        }
        return ""
    }

}

