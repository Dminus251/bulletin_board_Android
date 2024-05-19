package com.example.bulletin_board

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("create-posts/") //POST 요청을 create-post/로 보냄
    fun createPost(@Body postRequest: PostRequest): Call<ResponseBody>
    //매개변수는 객체를 HTTP의 body로 직렬화해서 보냄

    @POST("posts/")
    fun sendPost(@Body Posting: Posting): Call<ResponseBody>

//    @GET("posts/")
//    fun getPost(): Call<ResponseBody> // 응답을 List<Post>로 매핑


    @GET("posts/")
    fun getPost(): Call<ResponseBody>

}
