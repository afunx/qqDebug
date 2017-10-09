package com.ubt.ip.client.service;

import com.ubt.ip.client.bean.MotorBean;
import com.ubt.ip.client.constants.ClientConstants;
import com.ubt.ip.client.listener.MotorListener;

import java.util.List;

/**
 * 舵机Service
 */

public interface MotorService {

    /**
     * 获取舵机角度
     *
     * @param motorsId     舵机id数组
     * @param motorsDegree 舵机角度数组
     * @return {@link ClientConstants.ErrorCode} 执行结果
     */
    int getMotors(int[] motorsId, int[] motorsDegree);

    /**
     * 设置舵机角度（单个）
     *
     * @param motorBean     舵机bean
     * @param motorListener 舵机listener
     * @param opId          操作id，取消时使用
     * @return {@link ClientConstants.ErrorCode} 执行结果
     */
    int setMotor(MotorBean motorBean, MotorListener motorListener, int[] opId);

    /**
     * 设置舵机角度（多个）
     * （注意：舵机列表中，舵机延时时间和舵机运行时间，必需相同）
     *
     * @param motorBeanList 舵机bean列表
     * @param motorListener 舵机listener
     * @param opId          操作id，取消时使用
     * @return {@link ClientConstants.ErrorCode} 执行结果
     */
    int setMotors(List<MotorBean> motorBeanList, MotorListener motorListener, int[] opId);

    /**
     * 取消某个舵机操作
     *
     * @param opId  操作id
     * @param isSuc 是否取消成功
     * @return {@link ClientConstants.ErrorCode} 执行结果
     */
    int cancelOperation(int opId, boolean[] isSuc);

    /**
     * 进入回读模式
     *
     * @param isSuc 是否成功进入回读模式
     * @return {@link ClientConstants.ErrorCode} 执行结果
     */
    int enterReadMode(boolean[] isSuc);

    /**
     * 退出回读模式
     *
     * @param isSuc 是否成功进入回读模式
     * @return {@link ClientConstants.ErrorCode} 执行结果
     */
    int exitReadMode(boolean[] isSuc);

    /**
     * 检查回读模式状态
     *
     * @param readMode {@link ClientConstants.ReadMode} 回读模式状态
     * @return {@link ClientConstants.ErrorCode} 执行结果
     */
    int checkReadMode(int[] readMode);
}