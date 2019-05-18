package com.example.sudoku.http.service;


import com.example.sudoku.base.BaseBean;
import com.example.sudoku.bean.Rank;
import com.example.sudoku.bean.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiService {
    //String BASE_URL = "http://10.251.229.120:8080/";
    String BASE_URL = "http://47.100.235.15:8080/";

    @POST("/user/register")
    Observable<BaseBean<String>> register(@Body User user);

    @GET("/user/getcode")
    Observable<BaseBean> getCode(@QueryMap Map<String, String> map);

    @POST("/user/login")
    Observable<BaseBean<String>> login(@Body Map<String, String> map);

    @POST("/rank/submit")
    Observable<BaseBean> submit(@Body Map<String,String> map);

    @GET("/rank/getscore")
    Observable<BaseBean<List<List<Rank>>>> getScore();


}
