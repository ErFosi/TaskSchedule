package com.example.taskschedule.repositories

import com.example.taskschedule.data.Actividad
import kotlinx.coroutines.flow.Flow

interface ActividadesRepositoryInterface {
    /**
     * Obtiene todas las actividades de la base de datos
     */
    fun getActividadesStream(): Flow<List<Actividad>>

    /**
     * Obtiene la actividad que coincida con el Id
     */
    fun getActividadStream(id: Int): Flow<Actividad?>

    /**
     *Inserta una actividad a la base de datos
     */
    suspend fun insertActividad(item: Actividad)

    /**
     * Elimina una actividad de la base de datos
     */
    suspend fun deleteActividad(item: Actividad)

    /**
     * Modifica una actividad
     */
    suspend fun updateActividad(item: Actividad)
}