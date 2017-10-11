package com.ubt.ip.client.api;

import android.util.Log;

import com.ubt.ip.client.bean.MotorBean;
import com.ubt.ip.client.constants.ClientConstants;
import com.ubt.ip.client.listener.MotorListener;
import com.ubt.ip.client.service.MotorService;
import com.ubt.ip.client.util.MotorUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by afunx on 13/09/2017.
 */

public class MotorApi implements MotorService {

    private static final String TAG = "MotorApi";
    private final MotorUtil mMotorUtil;

    private MotorApi() {
        mMotorUtil = new MotorUtil();
        Log.e(TAG, "MotorApi VERSION v0.8.1");
    }

    /**
     * 获取MotorApi单例
     *
     * @return MotorApi单例
     */
    public static MotorApi get() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取舵机角度
     *
     * @param motorsId     舵机id数组
     * @param motorsDegree 舵机角度数组
     * @return {@link ClientConstants.ErrorCode} 执行结果
     */
    @Override
    public int getMotors(int[] motorsId, int[] motorsDegree) {

        int ret = mMotorUtil.getMotors(motorsId, motorsDegree);
        Log.e(TAG, "ret= " + ret + " getMotors() motorsId:  " + Arrays.toString(motorsId) + ", motorsDegree: " + Arrays.toString(motorsDegree));
        for (int i = 0; i < motorsId.length; i++) {
            if (motorsDegree[i] == -121) {
                motorsDegree[i] = -1000;
            }
        }
        return ret;
    }

    /**
     * 设置舵机角度（单个）
     *
     * @param motorBean     舵机bean
     * @param motorListener 舵机listener
     * @param opId          操作id，取消时使用
     * @return {@link ClientConstants.ErrorCode} 执行结果
     */
    @Override
    public int setMotor(final MotorBean motorBean, final MotorListener motorListener, int[] opId) {
        Log.i(TAG, "setMotor()");

        int ret = mMotorUtil.setMotor(motorBean, motorListener, opId);
        if (ret == ClientConstants.ErrorCode.SUCCESS) {
            new Thread() {
                @Override
                public void run() {
                    if (motorListener != null) {
                        try {
                            Thread.sleep(motorBean.getMotorRunMilli());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        motorListener.onComplete();
                    }
                }
            }.start();
        }
        return ret;
    }

    /**
     * 设置舵机角度（多个）
     * （注意：舵机列表中，舵机延时时间和舵机运行时间，必需相同）
     *
     * @param motorBeanList 舵机bean列表
     * @param motorListener 舵机listener
     * @param opId          操作id，取消时使用
     * @return {@link ClientConstants.ErrorCode} 执行结果
     */
    @Override
    public int setMotors(final List<MotorBean> motorBeanList, final MotorListener motorListener, int[] opId) {
        Log.i(TAG, "setMotors()");

        int ret = mMotorUtil.setMotors(motorBeanList, motorListener, opId);
        if (ret == ClientConstants.ErrorCode.SUCCESS) {
            new Thread() {
                @Override
                public void run() {
                    if (motorListener != null) {
                        try {
                            Thread.sleep(motorBeanList.get(0).getMotorRunMilli());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        motorListener.onComplete();
                    }
                }
            }.start();
        }

        return ret;
    }

    /**
     * 取消某个舵机操作
     *
     * @param opId  操作id
     * @param isSuc 是否取消成功
     * @return {@link ClientConstants.ErrorCode} 执行结果
     */
    @Override
    public int cancelOperation(int opId, boolean[] isSuc) {
        return mMotorUtil.cancelOperation(opId, isSuc);
    }

    /**
     * 进入回读模式
     *
     * @param isSuc 是否成功进入回读模式
     * @return {@link ClientConstants.ErrorCode} 执行结果
     */
    @Override
    public int enterReadMode(boolean[] isSuc) {
        Log.i(TAG, "enterReadMode()");
        return mMotorUtil.enterReadMode(isSuc);
    }

    /**
     * 退出回读模式
     *
     * @param isSuc 是否成功进入回读模式
     * @return {@link ClientConstants.ErrorCode} 执行结果
     */
    @Override
    public int exitReadMode(boolean[] isSuc) {
        Log.i(TAG, "exitReadMode()");
        return mMotorUtil.exitReadMode(isSuc);
    }

    /**
     * 检查回读模式状态
     *
     * @param readMode {@link ClientConstants.ReadMode} 回读模式状态
     * @return {@link ClientConstants.ErrorCode} 执行结果
     */
    @Override
    public int checkReadMode(int[] readMode) {
        Log.i(TAG, "checkReadMode()");
        return mMotorUtil.checkReadMode(readMode);
    }

    /**
     * 设置ip
     *
     * @param ip
     */
    public void setIp(String ip) {
        mMotorUtil.setUrl(String.format("http://%s", ip));
    }

    private static class SingletonHolder {
        private static final MotorApi INSTANCE = new MotorApi();
    }
}
