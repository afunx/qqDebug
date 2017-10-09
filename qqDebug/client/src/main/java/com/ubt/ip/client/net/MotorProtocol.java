package com.ubt.ip.client.net;

import com.ubt.ip.client.bean.MotorBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by afunx on 15/09/2017.
 */

public interface MotorProtocol {
    @GET("/motors")
    Call<int[]> getMotors(@Query("id") int id[]);

    @POST("/motor")
    Call<Integer> setMotor(@Body MotorBean motorBean);

    @POST("/motors")
    Call<Integer> setMotors(@Body List<MotorBean> motorBeenList);

    @POST("/cancel/motor")
    Call<Boolean> cancelOperation(@Query("id") int opId);

    @POST("/readmode/enter")
    Call<Boolean> enterReadMode();

    @POST("/readmode/exit")
    Call<Boolean> exitReadMode();

    @GET("/readmode/check")
    Call<Integer> checkReadMode();
}