package com.qqdebug.adapter;

import com.qqdebug.ActionDebugActivity;
import com.qqdebug.MainActivity;
import com.qqdebug.R;
import com.qqdebug.bean.ActionBean;
import com.qqdebug.utils.PrefUtils;
import com.ubt.ip.client.util.InputMethod;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * @Desc
 * @time 2017/9/13 16:36
 * @Author lixiangxiang
 */

public class EditActionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ActionBean> mBeanList;
    private Context mContext;

    public EditActionListAdapter(List<ActionBean> beanList, Context context) {
        mBeanList = beanList;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_action, parent, false);
        return new MyHolder(mView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder mHolder = (MyHolder) holder;
        mHolder.tvActionName.setText(mBeanList.get(position).getName());
        mHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeanList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,mBeanList.size()- position);
                PrefUtils.saveObject(PrefUtils.ACTION_LIST,mBeanList);
                InputMethod.closeInputMethod((MainActivity)mContext);
            }
        });
        mHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActionDebugActivity.class);
                intent.putExtra("action",mBeanList.get(position));
                InputMethod.closeInputMethod((MainActivity)mContext);
                mContext.startActivity(intent);

            }
        });
    }
    //刷新数据
    public void notifyData(List<ActionBean> lists){
        if (lists!=null && lists.size()>0){
            this.mBeanList = lists;
            notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        return mBeanList == null ? 0 : mBeanList.size();
    }

    //动作选择适配器
    public class MyHolder extends RecyclerView.ViewHolder {

        private RelativeLayout xrItem;
        private ImageView imageIcon;
        private TextView tvActionName;
        private LinearLayout xlBottom;
        private Button btnEdit;
        private Button btnDelete;
        public MyHolder(View view) {
            super(view);
            xrItem = (RelativeLayout) view.findViewById(R.id.xr_item);
            imageIcon = (ImageView) view.findViewById(R.id.image_icon);
            tvActionName = (TextView) view.findViewById(R.id.tv_action_name);
            xlBottom = (LinearLayout) view.findViewById(R.id.xl_bottom);
            btnEdit = (Button) view.findViewById(R.id.btn_edit);
            btnDelete = (Button) view.findViewById(R.id.btn_delete);
        }
    }
}
