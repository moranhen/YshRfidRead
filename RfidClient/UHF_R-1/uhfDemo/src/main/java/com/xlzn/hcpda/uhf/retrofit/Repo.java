package com.xlzn.hcpda.uhf.retrofit;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ysh_rfid")
public class Repo {
    @PrimaryKey
    @NonNull
    private String uid;

    @ColumnInfo(name ="name")
    private String name;

    @ColumnInfo(name ="kind")
    private String kind;

    @ColumnInfo(name ="createTime")
    private String createTime;

    @ColumnInfo(name ="lastUpdateTime")
    private String lastUpdateTime;

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName( String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind( String kind) {
        this.kind = kind;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime( String createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime( String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

}
