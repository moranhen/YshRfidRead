package com.xlzn.hcpda.uhf;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RFID {

    @PrimaryKey
    @NonNull
    public String uid;

    @ColumnInfo(name = "rfid_name")
    public String rfidName;

//    @ColumnInfo(name = "last_name")
//    public String lastName;
}
