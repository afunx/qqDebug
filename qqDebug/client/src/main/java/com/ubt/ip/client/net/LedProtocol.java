package com.ubt.ip.client.net;

import com.ubt.ip.client.bean.LedBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by afunx on 11/10/2017.
 */

public interface LedProtocol {
    @POST("/led")
    Call<Void> setLed(@Body LedBean ledBean);
}
