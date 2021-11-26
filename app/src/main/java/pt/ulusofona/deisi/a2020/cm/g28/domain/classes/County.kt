package pt.ulusofona.deisi.a2020.cm.g28.domain.classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class County(
    var name : String,
    var district : String,
    var riskDegree : String,
    var incidenceInterval : String,
    var incidence : String,
    var population : String,
    var confirmedCases : String,
    var comparativeFraction : String,
    var lastUpdate : String
)
{
    @PrimaryKey
    var uuid : String = UUID.randomUUID().toString()
}