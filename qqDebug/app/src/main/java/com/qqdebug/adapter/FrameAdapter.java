package com.qqdebug.adapter;

import com.qqdebug.ActionDebugActivity;
import com.qqdebug.R;
import com.qqdebug.bean.FrameBean;
import com.ubt.ip.client.api.LedApi;
import com.ubt.ip.client.api.MotorApi;
import com.ubt.ip.client.bean.LedBean;
import com.ubt.ip.client.bean.MotorBean;
import com.ubt.ip.client.listener.MotorListener;
import com.ubt.ip.client.net.MotorNet;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc
 * @time 2017/9/13 18:37
 * @Author lixiangxiang
 */

public class FrameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<FrameBean> mFrameBeens;
    private String TAG = "FrameAdapter";
    public FrameAdapter(Context context, List<FrameBean> frameBeens) {
        mContext = context;
        mFrameBeens = frameBeens;
        if (mFrameBeens != null && mFrameBeens.size() > 0)
            mFrameBeens.get(0).setSelect(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_meticulous_frame, parent, false);
        return new FrameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.i(this.getClass().getSimpleName(), "position==" + position);
        FrameViewHolder viewHolder = (FrameViewHolder) holder;
        if (mFrameBeens.get(position).isSelect()) {
            viewHolder.rlActionItem.setSelected(true);
        } else {
            viewHolder.rlActionItem.setSelected(false);
        }
        viewHolder.tvItemNum.setText(mFrameBeens.get(position).getName());
        viewHolder.tvItemTime.setText(String.valueOf(mFrameBeens.get(position).getRunTime()));
        viewHolder.rlActionItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPos = position;
                ((ActionDebugActivity) mContext).setSelectItem(selectPos);
                if (mFrameBeens.get(position).isSelect()) {
                    //// TODO: 2017/9/14  播放这个item
                    final List<MotorBean> lists = new ArrayList<>();
                    for (int i = 0; i < 7; i++) {
                        MotorBean bean = new MotorBean();
                        bean.setMotorRunMilli(mFrameBeens.get(position).getRunTime());
                        bean.setMotorAbsoluteDegree(mFrameBeens.get(position).getMotorBeen()[i].getDegree());
                        bean.setMotorId(mFrameBeens.get(position).getMotorBeen()[i].getId());
                        lists.add(bean);
                    }
                    // TODO afunx 播放Led灯效
                    final LedBean ledBean = new LedBean();
                    ledBean.setLedId(mFrameBeens.get(position).getLedBean().getLedId());
                    ledBean.setLedFrequency(mFrameBeens.get(position).getLedBean().getLedFrequency());
                    ledBean.setLedColor(mFrameBeens.get(position).getLedBean().getLedColor());
                    new Thread() {
                        @Override
                        public void run() {
                            MotorApi.get().setMotors(lists, new MotorListener() {
                                @Override
                                public void onStart() {
                                    Log.i(TAG, "onStart");
                                    // TODO afunx 播放Led灯效
                                    LedApi.get().setLed(ledBean);
                                }

                                @Override
                                public void onComplete() {
                                    Log.i(TAG, "onComplete");
                                }

                                @Override
                                public void onError(int errorCode) {
                                    Log.i(TAG, "onError");
                                }

                                @Override
                                public void onCancel() {

                                }
                            }, new int[7]);
                        }
                    }.start();
                } else {
                    //刷新舵机状态
                    ((ActionDebugActivity) mContext).changeFrame(position);
                }
                setItemSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFrameBeens == null ? 0 : mFrameBeens.size();
    }

    //选中某个item
    private void setItemSelected(int pos) {
        for (int i = 0; i < mFrameBeens.size(); i++) {
            if (pos == i) {
                mFrameBeens.get(pos).setSelect(true);
            } else {
                mFrameBeens.get(i).setSelect(false);
            }
        }
        notifyDataSetChanged();
    }

    //获取选中的帧 默认选中第一个
    private int selectPos = -1;

    public int selectPosition() {
        return selectPos;
    }

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
    }

    public List<FrameBean> getList() {
        return mFrameBeens;
    }

    public void setList(List<FrameBean> lists) {
        this.mFrameBeens = lists;
    }

    private static class FrameViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout rlMeticulousItem;
        private RelativeLayout rlActionItem;
        private TextView tvItemNum;
        private TextView tvItemTime;

        public FrameViewHolder(View view) {
            super(view);
            rlMeticulousItem = (LinearLayout) view.findViewById(R.id.rl_meticulous_item);
            rlActionItem = (RelativeLayout) view.findViewById(R.id.rl_action_item);
            tvItemNum = (TextView) view.findViewById(R.id.tv_item_num);
            tvItemTime = (TextView) view.findViewById(R.id.tv_item_time);
        }
    }
}
