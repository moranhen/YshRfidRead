package com.moyan.yshcardread.data

import com.google.gson.annotations.SerializedName

data class RepoSearchResponse(
    @SerializedName("code") val total: Int = 0,
    @SerializedName("msg") val rep: String = "",
    @SerializedName("data") val items: List<Repo> = emptyList()
)

data class Repo(
    @SerializedName("uid") val uid: String,
    @SerializedName("name") val name: String,
    @SerializedName("kind") val kind: String,
    @SerializedName("createTime") val createTime: String,
    @SerializedName("lastUpdateTime") val lastUpdateTime: String,
)
data class RepoSearchResponse1(
    @SerializedName("code") val total: Int = 0,
    @SerializedName("msg") val rep: String = "",
    @SerializedName("data") val item : Repo
)

//{
//    "code": 200,
//    "msg": "成功",
//    "data": {
//        "uid": "6",
//        "name": "123",
//        "kind": "0",
//        "createTime": null,
//        "lastUpdateTime": null
//    }
//}