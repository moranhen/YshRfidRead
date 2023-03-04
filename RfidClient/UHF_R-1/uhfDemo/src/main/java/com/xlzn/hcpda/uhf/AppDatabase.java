package com.xlzn.hcpda.uhf;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RFID.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appInstance;
    public AppDatabase(){

    }
    public static AppDatabase getAppInstance(Context context){
        if(appInstance ==null){
            synchronized (AppDatabase.class){
                if (appInstance==null){
                    appInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "database-name").build();
                }
            }
        }
        return appInstance;
    }
    public abstract RFIDDao rfidDao();
//    public abstract RepoDao repoDao();
}
