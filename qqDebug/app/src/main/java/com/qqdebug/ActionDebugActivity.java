package com.qqdebug;

import com.qqdebug.adapter.FrameAdapter;
import com.qqdebug.bean.ActionBean;
import com.qqdebug.bean.FrameBean;
import com.qqdebug.bean.MotorBean;
import com.ubt.ip.client.api.MotorApi;
import com.ubt.ip.client.listener.MotorListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Desc 动作调试 界面
 * @time 2017/9/13 15:00
 * @Author lixiangxiang
 */

public class ActionDebugActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvDegree;
    private SeekBar sbDegree;
    private TextView tvTime;
    private SeekBar sbTime;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btnCopy;
    private Button btnPaste;
    private Button btnSave;
    private Button btnReduceTime;
    private Button btnAddTime;
    private Button btnCreate;
    private Button btnRead;
    private Button btnReduceDegree;
    private Button btnAddDegree;
    private Button btnPlay;
    private Button btn7;
    private RecyclerView rvFrame;
    private TextView tvDegreeStart;
    private TextView tvDegreeEnd;
    private TextView tvTimeStart;
    private TextView tvTimeEnd;

    private int sbDegreeMinNum;//角度最小值
    private int sbDegreeMax;
    private int sbTimeMinNum;//时间最小值
    private int sbTimeMax;
    private ActionBean mBean;
    private List<FrameBean> mFrameBeens;//帧
    private FrameAdapter mFmAdapter;
    private FrameBean mFmCopy;//copy item
    private int mCopyPos;//copy position
    private Button[] engineButton;
    private int selectItem = -1;//选中的frame
    private int engine = 0; //选中的舵机
    private Button btnSure;
    private EditText editText;
    private Button btnEnterRead;
    private Button btnExitRead;
    private String TAG = "ActionDebugActivity";
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    selectItem += 1;
                    if (selectItem < mFrameBeens.size()) {
                        mFrameBeens.get(selectItem - 1).setSelect(false);
                        mFrameBeens.get(selectItem).setSelect(true);
                        mFmAdapter.setSelectPos(selectItem);
                        mFmAdapter.notifyDataSetChanged();
                        isPlayMode(mFrameBeens.get(selectItem));
                    } else {
                        isPlay = false;
                        btnPlay.setSelected(false);
                        selectItem -= 1;
                    }
                    break;
                case 1:
                    btnPlay.setSelected(false);
                    break;
                case 2:
                    mFmAdapter.notifyItemInserted(selectItem + 1);
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_debug);

        initView();

        initData();

        initListener();

        initSeekBar();
    }

    private void initSeekBar() {

        sbDegree.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvDegree.setText(sbDegreeMinNum + seekBar.getProgress() + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (selectItem == -1)
                    return;
                MotorBean[] motorBeen = mFrameBeens.get(selectItem).getMotorBeen();
                motorBeen[engine].setDegree(sbDegreeMinNum + seekBar.getProgress());
                Log.i("ActionDebugActivity", "sbDegreeMinNum+progress==" + (sbDegreeMinNum + seekBar.getProgress()));
            }
        });

        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvTime.setText(String.valueOf(sbTimeMinNum + seekBar.getProgress()));
                mFrameBeens.get(selectItem).setRunTime(sbTimeMinNum + seekBar.getProgress());
                mFmAdapter.notifyDataSetChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initData() {
        engineButton = new Button[7];
        engineButton[0] = btn1;
        engineButton[1] = btn2;
        engineButton[2] = btn3;
        engineButton[3] = btn4;
        engineButton[4] = btn5;
        engineButton[5] = btn6;
        engineButton[6] = btn7;

        for (int i = 0; i < 7; i++) {
            id[i] = i + 1;
        }
//        net = new MotorNet();
//        net.setBaseUrl("http://192.168.1.1/");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        mBean = (ActionBean) getIntent().getSerializableExtra("action");
        if (mBean == null) {
            mBean = new ActionBean();
            mFrameBeens = new ArrayList<>();
            setDegreeAndTime(-10, 20, 30, -1);
        } else {
            mFrameBeens = mBean.getFrameList();
            selectItem = 0;
            sbDegreeMinNum = -10;
            setDegreeAndTime(-10, 10, 20, 0);
        }
        mFmAdapter = new FrameAdapter(this, mFrameBeens);
        if (mFrameBeens.size() > 0) {
            mFmAdapter.setSelectPos(0);
        }
        setSbTime(100, 5000, 4900);
        setSelected(0);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvFrame.setLayoutManager(lm);
        rvFrame.setAdapter(mFmAdapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Collections.swap(mFrameBeens, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                mFmAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (selectItem == viewHolder.getAdapterPosition()) {
                    selectItem = -1;
                    mFmAdapter.setSelectPos(-1);
                }
                mFrameBeens.remove(viewHolder.getAdapterPosition());
                mFmAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                mFmAdapter.notifyItemRangeChanged(viewHolder.getAdapterPosition(), mFrameBeens.size() - viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rvFrame);
    }

    private void initListener() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btnCopy.setOnClickListener(this);
        btnPaste.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnReduceTime.setOnClickListener(this);
        btnAddTime.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnReduceDegree.setOnClickListener(this);
        btnAddDegree.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        btnEnterRead.setOnClickListener(this);
        btnExitRead.setOnClickListener(this);
    }

    private void initView() {
        tvDegree = (TextView) findViewById(R.id.tv_degree);
        sbDegree = (SeekBar) findViewById(R.id.sb_degree);
        tvTime = (TextView) findViewById(R.id.tv_time);
        sbTime = (SeekBar) findViewById(R.id.sb_time);
        btn1 = (Button) findViewById(R.id.btn_1);
        btn2 = (Button) findViewById(R.id.btn_2);
        btn3 = (Button) findViewById(R.id.btn_3);
        btn4 = (Button) findViewById(R.id.btn_4);
        btn5 = (Button) findViewById(R.id.btn_5);
        btn6 = (Button) findViewById(R.id.btn_6);
        btn7 = (Button) findViewById(R.id.btn_7);
        btnCopy = (Button) findViewById(R.id.btn_copy);
        btnPaste = (Button) findViewById(R.id.btn_paste);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnReduceTime = (Button) findViewById(R.id.btn_reduce_time);
        btnAddTime = (Button) findViewById(R.id.btn_add_time);
        btnCreate = (Button) findViewById(R.id.btn_create);
        btnRead = (Button) findViewById(R.id.btn_read);
        btnReduceDegree = (Button) findViewById(R.id.btn_reduce_degree);
        btnAddDegree = (Button) findViewById(R.id.btn_add_degree);
        btnPlay = (Button) findViewById(R.id.btn_play);
        rvFrame = (RecyclerView) findViewById(R.id.rv_frame);
        tvDegreeStart = (TextView) findViewById(R.id.tv_degree_start);
        tvDegreeEnd = (TextView) findViewById(R.id.tv_degree_end);
        tvTimeStart = (TextView) findViewById(R.id.tv_time_start);
        tvTimeEnd = (TextView) findViewById(R.id.tv_time_end);
        btnSure = (Button) findViewById(R.id.btn_sure);
        btnEnterRead = (Button) findViewById(R.id.btn_enter_read);
        btnExitRead = (Button) findViewById(R.id.btn_exit_read);
        editText = (EditText) findViewById(R.id.edit_text);
    }

    private int[] id = new int[7];
    private int[] degree = new int[7];
    private boolean[] readMode = new boolean[1];
    private boolean isPlay = false;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                //TODO implement
                Log.i("degree", "degree==" + 0);
                setDegreeAndTime(-10, 20, 30, 0);
                setSelected(0);
                break;
            case R.id.btn_2:
                //TODO implement
                //TODO implement
                setDegreeAndTime(-90, 90, 180, 1);
                setSelected(1);
                break;
            case R.id.btn_3:
                //TODO implement
                setDegreeAndTime(-100, 100, 200, 2);
                setSelected(2);
                break;
            case R.id.btn_4:
                //TODO implement
                setDegreeAndTime(-100, 100, 200, 3);
                setSelected(3);
                break;
            case R.id.btn_5:
                //TODO implement
                setDegreeAndTime(-10, 10, 20, 4);
                setSelected(4);
                break;
            case R.id.btn_6:
                //TODO implement
                setDegreeAndTime(-90, 10, 100, 5);
                setSelected(5);
                break;
            case R.id.btn_7:
                //TODO implement
                setDegreeAndTime(-10, 90, 100, 6);
                setSelected(6);
                break;
            case R.id.btn_copy:
                //TODO implement
                mCopyPos = mFmAdapter.selectPosition();
                if (mCopyPos == -1) {
                    Toast.makeText(this, "请先选择一个需要复制的帧", Toast.LENGTH_SHORT).show();
                    return;
                }
                mFmCopy = mFrameBeens.get(mCopyPos).clone();
                mFmCopy.setSelect(false);
                mFmCopy.setName("c" + mFmCopy.getName());
                btnCopy.setBackgroundResource(R.color.button_selected);
                btnPaste.setBackgroundResource(R.color.button_normal);
                break;
            case R.id.btn_paste:
                //TODO implement
                if (mCopyPos == -1 || mFmCopy == null) {
                    Toast.makeText(this, "请先选择一个需要复制的帧", Toast.LENGTH_SHORT).show();
                    return;
                }
                mFrameBeens.add(mCopyPos + 1, mFmCopy);
                mFmAdapter.notifyItemInserted(mCopyPos + 1);
                mFmAdapter.notifyItemRangeChanged(mCopyPos, mFrameBeens.size());
                mFmCopy = null;
                btnCopy.setBackgroundResource(R.color.button_normal);
                btnPaste.setBackgroundResource(R.color.button_selected);
                break;
            case R.id.btn_save:
                //TODO implement
                mBean.setFrameList(mFrameBeens);
                Intent intent = new Intent(this, CompleteActivity.class);
                intent.putExtra("action", mBean);
                startActivity(intent);
                break;
            case R.id.btn_add_degree:
                //TODO implement
                int progress = sbDegree.getProgress();
                Log.i("ActionDebugActivity", "progress==" + progress);
                if ((progress + sbDegreeMinNum) < sbDegreeMax) {
                    progress++;
                }
                if (selectItem != -1) {
                    mFrameBeens.get(selectItem).getMotorBeen()[engine].setDegree(progress + sbDegreeMinNum);
                }
                sbDegree.setProgress(progress);
                break;
            case R.id.btn_reduce_degree:
                //TODO implement
                int progressReduce = sbDegree.getProgress();
                if ((progressReduce + sbDegreeMinNum) > sbDegreeMinNum) {
                    progressReduce--;
                }
                if (selectItem != -1) {
                    mFrameBeens.get(selectItem).getMotorBeen()[engine].setDegree(progressReduce + sbDegreeMinNum);
                }
                sbDegree.setProgress(progressReduce);
                break;
            case R.id.btn_add_time:
                //TODO implement
                int progressTime = sbTime.getProgress();
                if ((progressTime + sbTimeMinNum) < sbTimeMax) {
                    progressTime++;
                }
                sbTime.setProgress(progressTime);
                break;
            case R.id.btn_reduce_time:
                //TODO implement
                int progressTimeReduce = sbTime.getProgress();
                if ((progressTimeReduce + sbTimeMinNum) > sbTimeMinNum) {
                    progressTimeReduce--;
                }
                sbTime.setProgress(progressTimeReduce);
                break;
            case R.id.btn_play:
                //TODO implement
                //从选中的项开始播放
                //播放完成之后 继续下一帧的播放
                //todo wifi 连接
                int pos = mFmAdapter.selectPosition();
                if (pos == -1)
                    return;
                if (!isPlay) {
                    isPlay = true;
                    isPlayMode(mFrameBeens.get(pos));
                } else {
                    isPlay = false;
                }
                btnPlay.setSelected(isPlay);
                break;
            case R.id.btn_create:
                //TODO implement
                //创建一个帧
                FrameBean fm = new FrameBean();
                mFrameBeens.add(fm);
                fm.setName(String.valueOf(mFrameBeens.size()));
                mFmAdapter.notifyItemInserted(mFrameBeens.size() - 1);
                break;
            case R.id.btn_read:
                //TODO implement
                //读取一帧  并且插入到选中帧的后面
                new Thread() {
                    @Override
                    public void run() {
                        int ret = MotorApi.get().getMotors(id, degree);
                        Log.e("afunx", "ret: " + ret + ", degree: " + Arrays.toString(degree));
                        if (ret != 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ActionDebugActivity.this, "读取失败，请重试", Toast.LENGTH_LONG).show();
                                }
                            });
                            return;
                        }
                        FrameBean fmCp = new FrameBean();
                        for (int i = 0; i < 7; i++) {
                            if (degree[i] == -1000) {
                                Log.e("afuxn", "hahahahahaha");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ActionDebugActivity.this, "读取失败，请重试！", Toast.LENGTH_LONG).show();
                                    }
                                });
                                return;
                            }
                            fmCp.getMotorBeen()[i].setDegree(degree[i]);
                            fmCp.getMotorBeen()[i].setId(id[i]);
                        }
                        fmCp.setName("r");
                        Log.i(TAG, "btn_read==FrameBean=" + fmCp.toString());
                        mFrameBeens.add(selectItem + 1, fmCp);
                        mHandler.sendEmptyMessage(2);
                    }
                }.start();

                break;
            case R.id.btn_sure:
                String name = editText.getText().toString().trim();
                if (!name.equals("")) {
                    mFrameBeens.get(selectItem).setName(name);
                    mFmAdapter.notifyItemChanged(selectItem);
                }
                break;
            case R.id.btn_enter_read:
                //进入回读状态
                isState(1);
                new Thread() {
                    @Override
                    public void run() {
                        final int j = MotorApi.get().enterReadMode(readMode);
                        Log.i(TAG, "btn_enter_read=" + readMode[0]);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (readMode[0])
                                    Toast.makeText(ActionDebugActivity.this, "进入成功", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(ActionDebugActivity.this, "进入失败错误码" + j, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }.start();

                break;
            case R.id.btn_exit_read:
                //退出回读状态
                isState(2);
                new Thread() {
                    @Override
                    public void run() {
                        final int j = MotorApi.get().exitReadMode(readMode);
                        Log.i(TAG, "btn_exit_read=" + readMode[0]);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (readMode[0])
                                    Toast.makeText(ActionDebugActivity.this, "退出成功", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(ActionDebugActivity.this, "退出失败错误码" + j, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }.start();
                break;
        }
    }

    private void isPlayMode(final FrameBean bean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<com.ubt.ip.client.bean.MotorBean> lists = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                    com.ubt.ip.client.bean.MotorBean bean1 = new com.ubt.ip.client.bean.MotorBean();
                    bean1.setMotorAbsoluteDegree(bean.getMotorBeen()[i].getDegree());
                    bean1.setMotorId(bean.getMotorBeen()[i].getId());
                    bean1.setMotorRunMilli(bean.getRunTime());
                    lists.add(bean1);
                }

                MotorApi.get().setMotors(lists, new MotorListener() {
                    @Override
                    public void onStart() {
                        Log.i(TAG, "onStart");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete" + "select=" + selectItem);
                        mHandler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onError(final int errorCode) {
                        mHandler.sendEmptyMessage(1);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ActionDebugActivity.this, "错误码" + errorCode, Toast.LENGTH_LONG).show();
                            }
                        });
                        Log.i(TAG, "onError");
                    }

                    @Override
                    public void onCancel() {
                        mHandler.sendEmptyMessage(1);
                    }
                }, new int[7]);
            }
        }).start();

    }

    private void isState(int pos) {
        if (pos == 1) {
            btnEnterRead.setBackgroundResource(R.color.button_selected);
            btnExitRead.setBackgroundResource(R.color.button_normal);
        } else {
            btnEnterRead.setBackgroundResource(R.color.button_normal);
            btnExitRead.setBackgroundResource(R.color.button_selected);
        }
    }

    /**
     * 修改 舵机状态
     */
    public void changeFrame(int pos) {
        FrameBean fb = mFrameBeens.get(pos);
        setSelected(0);
        setDegreeAndTime(-10, 20, 30, fb.getMotorBeen()[0].getDegree());
        sbTime.setProgress(fb.getRunTime() - sbTimeMinNum);
    }

    /**
     * 选中的item
     */
    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    /**
     * 选中的舵机
     */
    private void setSelected(int pos) {
        engine = pos;
        for (int i = 0; i < 7; i++) {
            if (pos == i) {
                engineButton[pos].setSelected(true);
                engineButton[pos].setBackgroundResource(R.color.button_selected);
            } else {
                engineButton[i].setBackgroundResource(R.color.button_normal);
                engineButton[i].setSelected(false);
            }
        }
    }

    /**
     * 设置 时间seeker
     *
     * @param tStart  初始时间
     * @param tEnd    最大时间
     * @param tLength 时间范围
     */
    private void setSbTime(int tStart, int tEnd, int tLength) {
        sbTime.setMax(tLength);
        sbTimeMinNum = tStart;
        sbTimeMax = tEnd;
        sbTime.setProgress(900);
        tvTime.setText((sbTimeMinNum + 900) + "");
        tvTimeStart.setText(String.valueOf(sbTimeMinNum));
        tvTimeEnd.setText(String.valueOf(tEnd));
    }

    /**
     * 设置进度条
     *
     * @param dStart  初始角度
     * @param dEnd    最大角度
     * @param dLength length
     */
    private void setDegreeAndTime(int dStart, int dEnd, int dLength, int degree) {
        sbDegreeMinNum = dStart;
        sbDegreeMax = dEnd;
        tvDegreeStart.setText(String.valueOf(sbDegreeMinNum));
        tvDegreeEnd.setText(String.valueOf(dEnd));
        sbDegree.setMax(dLength);

        if (selectItem != -1 && degree != -1) {
            int dd = 0;
            switch (degree) {
                case 0:
                    dd = mFrameBeens.get(selectItem).getMotorBeen()[0].getDegree();
                    break;
                case 1:
                    dd = mFrameBeens.get(selectItem).getMotorBeen()[1].getDegree();
                    break;
                case 2:
                    dd = mFrameBeens.get(selectItem).getMotorBeen()[2].getDegree();
                    break;
                case 3:
                    dd = mFrameBeens.get(selectItem).getMotorBeen()[3].getDegree();
                    break;
                case 4:
                    dd = mFrameBeens.get(selectItem).getMotorBeen()[4].getDegree();
                    break;
                case 5:
                    dd = mFrameBeens.get(selectItem).getMotorBeen()[5].getDegree();
                    break;
                case 6:
                    dd = mFrameBeens.get(selectItem).getMotorBeen()[6].getDegree();
                    break;
            }
            sbDegree.setProgress(dd - sbDegreeMinNum);
            tvDegree.setText(String.valueOf(dd));
        } else {
            tvDegree.setText(String.valueOf(sbDegreeMinNum));
            sbDegree.setProgress(sbDegreeMinNum);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
