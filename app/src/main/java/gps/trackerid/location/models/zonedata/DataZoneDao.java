package gps.trackerid.location.models.zonedata;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DataZoneDao {

    @Insert
    long insertZone(DataZone zone);

    @Update
    void updateZone(DataZone zone);

    @Delete
    void deleteZone(DataZone zone);

    @Query("SELECT * FROM zones")
    List<DataZone> getAllZones();

    @Query("SELECT * FROM zones WHERE id = :id")
    DataZone getZoneById(long id);

    @Query("DELETE FROM zones")
    void deleteAll();
}

