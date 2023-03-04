package com.xlzn.hcpda.uhf.retrofit;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Repo.class}, version = 1)
public abstract class AppDatabase1 extends RoomDatabase {
    private static AppDatabase1 appInstance;
    public AppDatabase1(){

    }
    public static AppDatabase1 getAppInstance(Context context){
        if(appInstance ==null){
            synchronized (AppDatabase1.class){
                if (appInstance==null){
                    appInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase1.class, "database1-name").build();
                }
            }
        }
        return appInstance;
    }
//    public abstract RFIDDao rfidDao();
    public abstract RepoDao repoDao();
}
