package com.ubt.ip.client.listener;

/**
 * 舵机Listener
 */

public interface MotorListener {

    /**
     * 舵机开始执行
     */
    void onStart();

    /**
     * 舵机执行成功
     */
    void onComplete();

    /**
     * 舵机执行失败
     *
     * @param errorCode 错误码
     */
    void onError(int errorCode);

    /**
     * 舵机执行取消
     */
    void onCancel();

}