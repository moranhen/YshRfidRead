package com.xlzn.hcpda.uhf;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface RFIDDao {
    @Query("SELECT * FROM RFID")
    Flowable<List<RFID>> getAll();

    @Query("SELECT * FROM RFID WHERE uid LIKE :RFIDId LIMIT 1")
    Flowable<RFID> loadAllById(String RFIDId);

    @Query("SELECT * FROM RFID WHERE  " +
            "rfid_name LIKE :first LIMIT 1")
    Flowable<RFID> findByName(String first);
    @Update
    public Single<Integer> updateUsers(RFID... RFIDs);
    @Insert
    Single<List<Long>> insertAll(RFID... RFIDs);

    @Delete
    Single<Integer> delete(RFID RFID);
}
