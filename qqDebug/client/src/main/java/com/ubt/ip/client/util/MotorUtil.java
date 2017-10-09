package com.ubt.ip.client.util;

import com.ubt.ip.client.bean.MotorBean;
import com.ubt.ip.client.listener.MotorListener;
import com.ubt.ip.client.net.MotorNet;
import com.ubt.ip.client.service.MotorService;

import java.util.List;

/**
 * Created by afunx on 15/09/2017.
 */

public class MotorUtil implements MotorService {

    private MotorNet mMotorNet = new MotorNet();

    @Override
    public int getMotors(int[] motorsId, int[] motorsDegree) {
        return mMotorNet.getMotors(motorsId, motorsDegree);
    }

    /**
     * 设置url
     * @param url
     */
    public void setUrl(String url){
        mMotorNet.setBaseUrl(url);
    }
    @Override
    public int setMotor(final MotorBean motorBean, final MotorListener motorListener, int[] opId) {
        return mMotorNet.setMotor(motorBean, motorListener, opId);
    }

    @Override
    public int setMotors(final List<MotorBean> motorBeanList, final MotorListener motorListener, int[] opId) {
        if (motorListener != null) {
            motorListener.onStart();
        }

        return mMotorNet.setMotors(motorBeanList, motorListener, opId);
    }

    @Override
    public int cancelOperation(int opId, boolean[] isSuc) {
        return mMotorNet.cancelOperation(opId, isSuc);
    }

    @Override
    public int enterReadMode(boolean[] isSuc) {
        return mMotorNet.enterReadMode(isSuc);
    }

    @Override
    public int exitReadMode(boolean[] isSuc) {
        return mMotorNet.exitReadMode(isSuc);
    }

    @Override
    public int checkReadMode(int[] readMode) {
        return mMotorNet.checkReadMode(readMode);
    }
}
