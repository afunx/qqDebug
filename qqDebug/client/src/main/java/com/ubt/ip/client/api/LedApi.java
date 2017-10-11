package com.ubt.ip.client.api;

import android.util.Log;

import com.ubt.ip.client.bean.LedBean;
import com.ubt.ip.client.service.LedService;
import com.ubt.ip.client.util.LedUtil;

/**
 * Created by afunx on 11/10/2017.
 */

public class LedApi implements LedService {

    private static final String TAG = "LedApi";
    private final LedUtil mLedUtil;

    private LedApi() {
        mLedUtil = new LedUtil();
        Log.e(TAG, "LedApi VERSION v0.8.1");
    }

    public static LedApi get() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public int setLed(LedBean ledBean) {
        Log.i(TAG, "setLed()");
        return mLedUtil.setLed(ledBean);
    }

    /**
     * 设置ip
     *
     * @param ip
     */
    public void setIp(String ip) {
        mLedUtil.setUrl(String.format("http://%s", ip));
    }

    private static class SingletonHolder {
        private static final LedApi INSTANCE = new LedApi();
    }
}
