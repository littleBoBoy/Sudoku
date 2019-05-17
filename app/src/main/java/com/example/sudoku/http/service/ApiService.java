package com.example.sudoku.http.service;


import com.example.sudoku.base.BaseBean;
import com.example.sudoku.bean.User;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiService {
    String BASE_URL = "http://10.251.233.57:8080/";

    //    https://wanandroid.com/wxarticle/chapters/json
    //    方法： GET
//
//    @POST
//    Observable<BaseBean> getCode(@Url String url, @Body Map<String, String> map);
//
//    @GET("{version}/users/{pk}")
//    Observable<BaseBean<PersonalBean>> getPersonal(@Path("version") String version, @Path("pk") String pk);
//
//    @GET("{version}/users/search/departments")
//    Observable<BaseBean<List<DepartmentBean>>> getDepartments(@Path("version") String version);
//
//    @PUT("{version}/users")
//    Observable<BaseBean<PersonalBean>> receivePersonal(@Path("version") String version, @Body PersonalBean personalBean);
//
//    @POST("{version}/orders")
//    Observable<BaseBean> createOrder(@Path("version") String version, @Body CreateOrderBean createOrderBean);
//
////    @GET("{version}/orders/search?{page}&{size}&{user_id}")
////    Observable<BaseBean<OrderListBean>> getOrderList(@Path("version") String version,@Path("page") String page,
////                                                     @Path("size")String size,@Path("user_id")String user_id);
//
//    @GET("{version}/orders/search")
//    Observable<BaseBean<OrderListBean>> getOrderList(@Path("version") String version, @QueryMap Map<String, String> map);

    @POST("/user/register")
    Observable<BaseBean<String>> register(@Body User user);

    @GET("/user/getcode")
    Observable<BaseBean> getCode(@QueryMap Map<String, String> map);

    @POST("/user/login")
    Observable<BaseBean<String>> login(@Body Map<String, String> map);
}
