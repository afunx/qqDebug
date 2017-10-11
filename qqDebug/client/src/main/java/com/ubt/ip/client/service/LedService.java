package com.ubt.ip.client.service;

import com.ubt.ip.client.bean.LedBean;
import com.ubt.ip.client.constants.ClientConstants;

/**
 * Created by afunx on 10/10/2017.
 * Led Service
 */

public interface LedService {
    /**
     * 设置Led
     *
     * @param ledBean led bean
     * @return {@link ClientConstants.ErrorCode} 执行结果
     */
    int setLed(LedBean ledBean);
}
