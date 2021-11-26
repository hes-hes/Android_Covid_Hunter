package pt.ulusofona.deisi.a2020.cm.g28.domain.classes

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class CovidTest(
    var date: String,
    var local: String,
    var result: String,
    var photo: ByteArray
) : Parcelable
{
    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString()
}