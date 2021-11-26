package pt.ulusofona.deisi.a2020.cm.g28.domain.logic

import kotlinx.coroutines.runBlocking
import pt.ulusofona.deisi.a2020.cm.g28.data.local.room.dao.CovidTestDao
import pt.ulusofona.deisi.a2020.cm.g28.domain.classes.CovidTest
import java.util.*

class TestListLogic(private val storage: CovidTestDao) {

    fun getAllList(): List<CovidTest> {
        return getList(storage)
    }

    private fun getList(storage: CovidTestDao): List<CovidTest> = runBlocking{
        return@runBlocking storage.getAllTest()
    }

}