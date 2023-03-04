package com.xlzn.hcpda.uhf.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RepoResponse {
    @SerializedName("code")
    int code;

    @SerializedName("msg")
    String msg;


    @SerializedName("data")
    ArrayList<Repo> results;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<Repo> getResults() {
        return results;
    }
}
