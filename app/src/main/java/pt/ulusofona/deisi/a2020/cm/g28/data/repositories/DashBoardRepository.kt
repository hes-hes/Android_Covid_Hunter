package pt.ulusofona.deisi.a2020.cm.g28.data.repositories

import pt.ulusofona.deisi.a2020.cm.g28.data.local.room.dao.DashBoardDao
import retrofit2.Retrofit

class DashBoardRepository(val local : DashBoardDao, val remote : Retrofit)