package com.mambo.template.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mambo.template.data.model.Meal

@Database(
    entities = [Meal::class],
    exportSchema = false,
    version = 1
)
@TypeConverters(Converters::class)
abstract class HealthiDatabase : RoomDatabase() {

    abstract fun mealsDao(): MealsDao

}