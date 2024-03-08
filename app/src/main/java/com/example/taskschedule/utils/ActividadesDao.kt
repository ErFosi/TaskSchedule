package com.example.taskschedule.utils
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskschedule.data.Actividad
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(actividad: Actividad)

    @Update
    suspend fun update(actividad:Actividad)

    @Delete
    suspend fun delete(actividad:Actividad)

    @Query("SELECT * from actividades WHERE id=:id")
    fun getActividad(id:Int): Flow<Actividad>

    @Query("SELECT * from actividades ORDER BY tiempo")
    fun getActividades(): Flow<List<Actividad>>
}