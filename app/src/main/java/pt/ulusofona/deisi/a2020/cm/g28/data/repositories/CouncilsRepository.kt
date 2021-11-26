package pt.ulusofona.deisi.a2020.cm.g28.data.repositories

import pt.ulusofona.deisi.a2020.cm.g28.data.local.room.dao.CouncilsDao
import retrofit2.Retrofit

class CouncilsRepository (val local : CouncilsDao, val remote : Retrofit)