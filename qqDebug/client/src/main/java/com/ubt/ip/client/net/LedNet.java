package com.ubt.ip.client.net;

import com.ubt.ip.client.bean.LedBean;
import com.ubt.ip.client.constants.ClientConstants;
import com.ubt.ip.client.service.LedService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by afunx on 11/10/2017.
 */

public class LedNet implements LedService{

    private String mBaseUrl = "http://127.0.0.1";

    private final int mBasePort = 8266;

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(getBaseUrl() + ":" + mBasePort)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public int setLed(LedBean ledBean) {
        if (mBaseUrl == null) {
            return ClientConstants.ErrorCode.BASE_URL_NULL;
        }

        Retrofit retrofit = getRetrofit();

        LedProtocol ledProtocol = retrofit.create(LedProtocol.class);
        Call<Void> call = ledProtocol.setLed(ledBean);
        try {
            call.execute();
            return ClientConstants.ErrorCode.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.IOEXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.UNKNOWN_ERR;
        }
    }
}
