package com.karthik.myweather.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.karthik.myweather.data.entities.LocationEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface LocationEntityDao {
    @Query("SELECT * FROM locations where locationId = :id")
    fun getLocation(id: Int): Single<LocationEntity?>?

    @get:Query("SELECT * FROM locations")
    val all: Single<List<LocationEntity>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(locationEntity: LocationEntity?): Completable?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(locationEntities: List<LocationEntity?>?): Completable?

    @Query("DELETE FROM locations")
    fun deleteAll(): Completable
}