package com.example.pd_p4_app;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user WHERE uid IN (:plantId)")
    User getUserById(int plantId);

    @Query("SELECT COUNT(*) from user")
    int countUsers();

    @Query("SELECT * FROM user ORDER BY plant_currenthumidity-plant_minhumidity")
    List<User> getSortedUsers();

    @Insert
    void addUser(User user);

    @Delete
    void deleteUser(User user);

    @Update
    void updateUser(User user);
}
