package com.qqdebug.bean;

import java.io.Serializable;

/**
 * @Desc
 * @time 2017/9/13 15:17
 * @Author lixiangxiang
 */

public class FrameBean implements Cloneable,Serializable {


    private int runTime =1000;

    private MotorBean[] mMotorBeen = new MotorBean[7];

    public FrameBean(){
        for (int i = 0 ; i<7 ; i++){
            mMotorBeen[i] = new MotorBean();
        }
        mMotorBeen[0].setId(1);
        mMotorBeen[0].setDegree(0);
        mMotorBeen[1].setId(2);
        mMotorBeen[1].setDegree(0);
        mMotorBeen[2].setId(3);
        mMotorBeen[2].setDegree(0);
        mMotorBeen[3].setId(4);
        mMotorBeen[3].setDegree(0);
        mMotorBeen[4].setId(5);
        mMotorBeen[4].setDegree(0);
        mMotorBeen[5].setId(6);
        mMotorBeen[5].setDegree(0);
        mMotorBeen[6].setId(7);
        mMotorBeen[6].setDegree(0);
    }

    private boolean isSelect;//是否被选中

    private String name;//帧名称名称

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public MotorBean[] getMotorBeen() {
        return mMotorBeen;
    }

    public void setMotorBeen(MotorBean[] motorBeen) {
        mMotorBeen = motorBeen;
    }

    @Override
    public FrameBean clone(){
        try {
            return (FrameBean) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return new FrameBean();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("time=").append(runTime);
        for (int i =0 ;i<7 ;i++){
            sb.append("id=").append(mMotorBeen[i].getId()).append("degree=").append(mMotorBeen[i].getDegree());
        }
        return sb.toString();
    }
}
