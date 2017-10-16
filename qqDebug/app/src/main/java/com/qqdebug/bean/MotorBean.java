package com.qqdebug.bean;

import java.io.Serializable;

/**
 * @Desc  角度
 * @time 2017/9/13 15:12
 * @Author lixiangxiang
 */

public class MotorBean implements Cloneable,Serializable {

    private int id;

    private int degree;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    @Override
    public MotorBean clone() {
        try {
            return (MotorBean) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
