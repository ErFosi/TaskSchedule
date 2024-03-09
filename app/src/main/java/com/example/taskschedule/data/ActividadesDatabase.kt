package com.example.taskschedule.data
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskschedule.utils.ActividadesDao
import android.content.Context
import androidx.room.Room
import androidx.room.TypeConverters
import com.example.taskschedule.utils.DateConverter


@Database(entities = [Actividad::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class ActividadesDatabase: RoomDatabase() {
    abstract fun actividadDao(): ActividadesDao

}