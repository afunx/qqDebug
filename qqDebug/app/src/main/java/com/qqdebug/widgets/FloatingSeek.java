package com.qqdebug.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by Administrator on 2017/2/16.
 */

public class FloatingSeek extends SeekBar implements
        ProgressHintDelegate.SeekBarHintDelegateHolder {


    private ProgressHintDelegate hintDelegate;
    private int mix;
    private int max;


    public FloatingSeek(Context context) {
        this(context,null);
    }

    public FloatingSeek(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public FloatingSeek(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }


    private void init(AttributeSet attrs, int defStyle) {
        hintDelegate = new HorizontalProgressHintDelegate(this, attrs, defStyle);
    }


    @Override public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        super.setOnSeekBarChangeListener(hintDelegate.setOnSeekBarChangeListener(l));
    }

    @Override
    public ProgressHintDelegate getHintDelegate() {
        return hintDelegate;
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress-mix);

    }


    public void setMix(int mix){
        this.mix=mix;
        hintDelegate.setPopupMin(mix);
//        super.setMax(Math.abs(mix)+Math.abs(max));
    }

    public void setMax(int max) {
        this.max = max;
        super.setMax(Math.abs(max - mix));
    }




}
