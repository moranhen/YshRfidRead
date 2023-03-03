package com.moyan.yshcardread.data.api

import android.util.Log
import com.google.gson.Gson
import com.moyan.yshcardread.data.RepoSearchResponse
import com.moyan.yshcardread.data.RepoSearchResponse1
import com.moyan.yshcardread.data.SSLSocketClient
import com.moyan.yshcardread.data.ServiceBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager


interface RfidService {
    @GET("ysh/user")
    suspend fun getRepos(): RepoSearchResponse

    @GET("ysh/user")
    suspend fun getRepo(@Query("uid") query: String): RepoSearchResponse
    @GET("ysh/user")
    suspend fun getRepoByName(@Query("name") query: String): RepoSearchResponse

    @POST("ysh/user")
    fun addRfid(@Body userData: RequestBody): Call<RepoSearchResponse1>

    @DELETE("ysh/user/{uid}")
    suspend fun deleteRepo(@Path("uid") uid: String) :RepoSearchResponse1

    @PUT("ysh/user/{uid}")
    fun updateRfid(@Path("uid") uid: String,@Body userData: RequestBody): Call<RepoSearchResponse1>

    companion object {
        private const val BASE_URL = "http://xxxxx/"

        fun createGithubApi(): RfidService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .sslSocketFactory(
                    SSLSocketClient.getSSLSocketFactory()!!,
                    object : X509TrustManager {
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {}
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {}
                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    })
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RfidService::class.java)
        }

        fun addRfid(userData: RfidPost, onResult: (RepoSearchResponse1?) -> Unit){
            val retrofit = ServiceBuilder.buildService(RfidService::class.java)
            val body: RequestBody = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                Gson().toJson(userData)
            )
            retrofit.addRfid(body).enqueue(
                object : Callback<RepoSearchResponse1> {
                    override fun onFailure(call: Call<RepoSearchResponse1>, t: Throwable) {
                        onResult(null)
                    }
                    override fun onResponse( call: Call<RepoSearchResponse1>, response: Response<RepoSearchResponse1>) {
                        val addedUser = response.body()
                        Log.d("Post Res: ",addedUser?.item.toString())
                        onResult(addedUser)
                    }
                }
            )
        }
        fun changeRfid(uid:String,userData: RfidPost, onResult: (RepoSearchResponse1?) -> Unit) {
            val retrofit = ServiceBuilder.buildService(RfidService::class.java)
            val body: RequestBody = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                Gson().toJson(userData)
            )
            retrofit.updateRfid(uid,body).enqueue(
                object : Callback<RepoSearchResponse1> {
                    override fun onFailure(call: Call<RepoSearchResponse1>, t: Throwable) {
                        onResult(null)
                    }
                    override fun onResponse( call: Call<RepoSearchResponse1>, response: Response<RepoSearchResponse1>) {
                        val addedUser = response.body()
                        Log.d("Post Res: ",addedUser?.item.toString())
                        onResult(addedUser)
                    }
                }
            )
        }

    }
}