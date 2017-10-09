package com.qqdebug.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Desc
 * @time 2017/9/13 16:27
 * @Author lixiangxiang
 */

public class ActionBean implements Serializable,Cloneable {

    //帧集合
    private List<FrameBean> frameList;

    private String   name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FrameBean> getFrameList() {
        return frameList;
    }

    public void setFrameList(List<FrameBean> frameList) {
        this.frameList = frameList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name=").append(name);
        for (int i= 0;i<frameList.size();i++){
            sb.append(frameList.get(i).toString());
        }
        return sb.toString();
    }
}
