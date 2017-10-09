package com.ubt.ip.client.bean;

/**
 * 舵机bean
 */
public class MotorBean {

    /**
     * 舵机id
     */
    private int mMotorId;

    /**
     * 舵机绝对角度
     */
    private int mMotorAbsoluteDegree;

    /**
     * 舵机延时时间（单位：毫秒）
     */
    private int mMotorDelayMilli;

    /**
     * 舵机运行时间（单位：毫秒）
     */
    private int mMotorRunMilli;

    /**
     * 获取舵机id
     *
     * @return 舵机id
     */
    public int getMotorId() {
        return mMotorId;
    }

    /**
     * 设置舵机id
     *
     * @param motorId 舵机id
     */
    public void setMotorId(int motorId) {
        mMotorId = motorId;
    }

    /**
     * 获取舵机绝对角度
     *
     * @return 舵机绝对角度
     */
    public int getMotorAbsoluteDegree() {
        return mMotorAbsoluteDegree;
    }

    /**
     * 设置舵机绝对角度
     *
     * @param motorAbsoluteDegree 舵机绝对角度
     */
    public void setMotorAbsoluteDegree(int motorAbsoluteDegree) {
        mMotorAbsoluteDegree = motorAbsoluteDegree;
    }

    /**
     * 获取舵机执行延迟时间（单位：毫秒）
     *
     * @return 舵机执行延迟时间（单位：毫秒）
     */
    public int getMotorDelayMilli() {
        return mMotorDelayMilli;
    }

    /**
     * 设置舵机执行延迟时间（单位：毫秒）
     *
     * @param motorDelayMilli （单位：毫秒）
     */
    public void setMotorDelayMilli(int motorDelayMilli) {
        mMotorDelayMilli = motorDelayMilli;
    }

    /**
     * 获取舵机运行时间（单位：毫秒）
     *
     * @return 舵机运行时间（单位：毫秒）
     */
    public int getMotorRunMilli() {
        return mMotorRunMilli;
    }

    /**
     * 设置舵机运行时间（单位：毫秒）
     *
     * @param motorRunMilli 舵机运行时间（单位：毫秒）
     */
    public void setMotorRunMilli(int motorRunMilli) {
        mMotorRunMilli = motorRunMilli;
    }

}
