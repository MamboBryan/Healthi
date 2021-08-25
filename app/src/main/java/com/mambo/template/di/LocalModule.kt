package com.mambo.template.di

import android.app.Application
import androidx.room.Room
import com.mambo.template.data.local.HealthiDatabase
import com.mambo.template.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
    ): HealthiDatabase =
        Room.databaseBuilder(app, HealthiDatabase::class.java, Constants.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providesMealsDao(database: HealthiDatabase) = database.mealsDao()

}