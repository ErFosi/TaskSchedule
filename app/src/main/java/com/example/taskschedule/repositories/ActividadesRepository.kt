package com.example.taskschedule.repositories

import com.example.taskschedule.data.Actividad
import com.example.taskschedule.utils.ActividadesDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActividadesRepository @Inject constructor(private val actividadesDao: ActividadesDao) : ActividadesRepositoryInterface{
    override suspend fun deleteActividad(actividad: Actividad) = actividadesDao.delete(actividad)


    override fun getActividadStream(id: Int): Flow<Actividad?> = actividadesDao.getActividad(id)


    override fun getActividadesStream(): Flow<List<Actividad>> = actividadesDao.getActividades()

    override suspend fun insertActividad(actividad: Actividad) = actividadesDao.insert(actividad)

    override suspend fun updateActividad(actividad: Actividad) = actividadesDao.update(actividad)

    override fun getActividadesPorFecha(fecha: LocalDate): Flow<List<Actividad>> = actividadesDao.getActividadesPorFecha(fecha)

}