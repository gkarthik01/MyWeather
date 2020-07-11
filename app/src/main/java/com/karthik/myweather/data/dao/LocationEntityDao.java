package com.karthik.myweather.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.karthik.myweather.data.entities.LocationEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface LocationEntityDao {

    @Query("SELECT * FROM locations where locationId = :id")
    Single<LocationEntity> getLocation(int id);

    @Query("SELECT * FROM locations")
    Single<List<LocationEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable add(LocationEntity locationEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addAll(List<LocationEntity> locationEntities);

    @Query("DELETE FROM locations")
    Completable deleteAll();
}
