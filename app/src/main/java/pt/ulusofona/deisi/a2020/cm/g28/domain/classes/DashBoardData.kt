package pt.ulusofona.deisi.a2020.cm.g28.domain.classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class DashBoardData(
    var newCases : String,
    var activeCases : String,
    var hospitalized : String,
    var deaths : String,
    var confirmedCases : String,
    var recovered : String,
    var criticals : String
) {

    @PrimaryKey
    var dateTime : String = ""

    init {

        val cal = Calendar.getInstance()
        val dateTimeParts = cal.time.toString().split(" ")
        dateTime = dateTimeParts[2] + " " +
                   dateTimeParts[1] + " " +
                   dateTimeParts[5] + " - " +
                   dateTimeParts[3]

    }

}