package com.xlzn.hcpda.uhf.retrofit;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RepoDao {
    @Query("SELECT * FROM ysh_rfid")
    List<Repo> getAll();
    @Query("SELECT * FROM ysh_rfid WHERE uid LIKE :RFIDId LIMIT 1")
    Repo loadAllById(String RFIDId);
    @Query("SELECT * FROM ysh_rfid WHERE  " +
            "name LIKE :first LIMIT 1")
    Repo findByName(String first);
    @Update
    public Integer updateUsers(Repo... Repos);
    @Insert
    Long insert(Repo repo);

    @Delete
    Integer delete(Repo repo);
}
