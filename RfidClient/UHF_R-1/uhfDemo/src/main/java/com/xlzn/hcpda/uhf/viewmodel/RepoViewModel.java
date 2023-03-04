package com.xlzn.hcpda.uhf.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.xlzn.hcpda.uhf.retrofit.AppDatabase1;
import com.xlzn.hcpda.uhf.retrofit.Repo;
import com.xlzn.hcpda.uhf.retrofit.RepoDao;
import com.xlzn.hcpda.uhf.retrofit.Repository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class RepoViewModel extends AndroidViewModel {
    private AppDatabase1 appDataBase;
    private RepoDao repoDao;
    public RepoViewModel(@NonNull Application application) {
        super(application);
        appDataBase = AppDatabase1.getAppInstance(application);
        repoDao = appDataBase.repoDao();
    }

    public Single<Boolean> insertRepo(Repo repo) {
        return Repository.getInstance().insertRepo(repoDao, repo);
    }

    public LiveData<ArrayList<Repo>> getRepos() {
        return Repository.getInstance().getAllPosts();
    }

    public Flowable<List<Repo>> getLocalRepos() {
        return Repository.getInstance().getAllLocalRepos(repoDao);
    }
    public Flowable<Repo> getlocalRepo(String uid){
        return Repository.getInstance().getById(repoDao,uid);
    }
    public Single<Integer> updateLocalRepo(Repo repo){
        return Repository.getInstance().updateRepo(repoDao,repo);
    }
    public Single<Integer> deleteLocalRepo(Repo repo){
        return Repository.getInstance().deleLocalRepo(repoDao,repo);
    }
}
