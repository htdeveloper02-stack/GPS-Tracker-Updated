package gps.trackerid.location.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import gps.trackerid.location.models.users.DataUser;

@Dao
public interface DataUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(DataUser user);

    @Update
    void updateUser(DataUser user);

    @Delete
    void deleteUser(DataUser user);
    @Query("UPDATE users SET isSelected = :name WHERE code = :code")
    void updateUserByCode(String code, boolean name);

    @Query("DELETE FROM users WHERE code = :code")
    void deleteUserByCode(String code);

    @Query("SELECT * FROM users WHERE code = :code LIMIT 1")
    DataUser getUserByCode(String code);
    @Query("SELECT COUNT(*) FROM users WHERE code = :code")
    int exists(String code);
    @Query("SELECT * FROM users")
    List<DataUser> getAllUsers();

}

