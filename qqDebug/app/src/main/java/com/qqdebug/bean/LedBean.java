package com.qqdebug.bean;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by afunx on 10/10/2017.
 * <p>
 * Led Bean
 */

public class LedBean implements Cloneable, Serializable {

    /**
     * Led id
     */
    private int mLedId;

    /**
     * Led颜色
     */
    private int mLedColor;

    /**
     * Led频率
     */
    private int mLedFrequency;

    public int getLedId() {
        return mLedId;
    }

    public void setLedId(int ledId) {
        mLedId = ledId;
    }

    public int getLedColor() {
        return mLedColor;
    }

    public void setLedColor(int ledColor) {
        mLedColor = ledColor;
    }

    public int getLedFrequency() {
        return mLedFrequency;
    }

    public void setLedFrequency(int ledFrequency) {
        mLedFrequency = ledFrequency;
    }

    @Override
    public LedBean clone() {
        try {
            return (LedBean) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
