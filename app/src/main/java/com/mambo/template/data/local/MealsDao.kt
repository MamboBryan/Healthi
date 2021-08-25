package com.mambo.template.data.local

import androidx.room.*
import com.mambo.template.data.model.Meal
import kotlinx.coroutines.flow.Flow

@Dao
interface MealsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(meals: List<Meal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: Meal)

    @Update
    suspend fun update(meal: Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @Query("SELECT * FROM meals")
    fun getAll(): Flow<List<Meal>>

    @Query("SELECT * FROM meals WHERE day LIKE '%' || :query || '%'")
    fun getPoemsByDate(query: String): Flow<List<Meal>>

    @Query("DELETE  FROM meals")
    suspend fun deleteAll()

}