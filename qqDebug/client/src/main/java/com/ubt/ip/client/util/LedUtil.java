package com.ubt.ip.client.util;

import com.ubt.ip.client.bean.LedBean;
import com.ubt.ip.client.net.LedNet;
import com.ubt.ip.client.service.LedService;

/**
 * Created by afunx on 11/10/2017.
 */

public class LedUtil implements LedService {

    private LedNet mLedNet = new LedNet();

    /**
     * 设置url
     *
     * @param url
     */
    public void setUrl(String url) {
        mLedNet.setBaseUrl(url);
    }

    @Override
    public int setLed(LedBean ledBean) {
        return mLedNet.setLed(ledBean);
    }
}
