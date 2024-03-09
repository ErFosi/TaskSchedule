package com.example.taskschedule.di
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.taskschedule.data.ActividadesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, ActividadesDatabase::class.java, "actividades_db")
            .createFromAsset("actividades_db.db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideActividades(db:ActividadesDatabase)=db.actividadDao()
}