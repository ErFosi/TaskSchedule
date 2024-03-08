package com.example.taskschedule.data
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskschedule.utils.ActividadesDao
import android.content.Context
import androidx.room.Room


@Database(entities = [Actividad::class], version = 1, exportSchema = false)
abstract class ActividadesDatabase: RoomDatabase() {
    abstract fun actividadDao(): ActividadesDao

}