package com.xlzn.hcpda.uhf.retrofit;


import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RfidService {
    @GET("ysh/user")
    Observable<RepoResponse> getRfids();
}
