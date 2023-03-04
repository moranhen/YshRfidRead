package com.xlzn.hcpda.uhf.retrofit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Repository {
    public static Repository repository;
    RfidService rfidService;

    private Repository() {
        rfidService = RetrofitServiceManager.getInstance().create(RfidService.class);
    }

    public static Repository getInstance() {

        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    public Single<Boolean> insertRepo(RepoDao repoDao,  Repo repo) {

        Single<Boolean> addPost = Single.create(emitter -> {

            Long l = repoDao.insert(repo);
            if (l != -1)
                emitter.onSuccess(true);
            else
                emitter.onSuccess(false);
        });

        return addPost.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public LiveData<ArrayList<Repo>> getAllPosts() {

        MutableLiveData<ArrayList<Repo>> repos = new MutableLiveData<>();

//        postHandler.getPosts().enqueue(new Callback<ArrayList<Post>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
//                posts.setValue(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
//                posts.setValue(null);
//            }
//        });

        rfidService.getRfids().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(repoResponse -> {
            repos.setValue(repoResponse.results);
        },throwable -> {
            repos.setValue(null);
            throwable.printStackTrace();
        });


        return repos;
    }

    public Flowable<List<Repo>> getAllLocalRepos(RepoDao repoDao) {

        Flowable<List<Repo>> localPostsSingle = Flowable.create(emitter -> {
            List<Repo> localPosts = repoDao.getAll();
            emitter.onNext(localPosts);
            emitter.onComplete();
        },BackpressureStrategy.BUFFER);

        return localPostsSingle.distinctUntilChanged().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Repo> getById(RepoDao repoDao , String uid){
        Flowable<Repo> localRepo = Flowable.create(emitter -> {
            Repo repo = repoDao.loadAllById(uid);
            emitter.onNext(repo);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
        return localRepo.distinctUntilChanged().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    public Single<Integer> updateRepo(RepoDao repoDao, Repo repo){
        Single<Integer> updateR = Single.create(emitter ->{
            Integer res = repoDao.updateUsers(repo);
            if (res !=0){
                emitter.onSuccess(1);
            } else {
                emitter.onSuccess(0);
            }
        });
        return updateR.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    public Single<Integer> deleLocalRepo(RepoDao repoDao,Repo repo){
        Single<Integer> deleteR = Single.create(emitter ->{
            Integer res = repoDao.delete(repo);
            if (res !=0){
                emitter.onSuccess(1);
            } else {
                emitter.onSuccess(0);
            }
        });
        return deleteR.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
