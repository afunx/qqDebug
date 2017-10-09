package com.qqdebug;

import com.qqdebug.bean.ActionBean;
import com.qqdebug.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc
 * @time 2017/9/13 16:33
 * @Author lixiangxiang
 */

public class CompleteActivity extends Activity {

    private Toolbar toolTop;
    private TextView tvName;
    private EditText mEditText;
    private Button btnSave;
    private ActionBean mActionBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        initView();

        initData();

        initListener();
    }

    private void initListener() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String edit =  mEditText.getText().toString().trim();
                if (edit.equals("")){
                    Toast.makeText(CompleteActivity.this,"命名不能为空!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isAdd(edit)){
                    Toast.makeText(CompleteActivity.this,"命名重复!",Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(CompleteActivity.this,MainActivity.class));
                    finish();
                }
            }
        });
    }

    private boolean isAdd(String edit){
       List<ActionBean> lists = (List<ActionBean>) PrefUtils.readObject(PrefUtils.ACTION_LIST);
        boolean isAdd = true;
        if (lists == null){
            lists = new ArrayList<>();
            mActionBean.setName(edit);
            lists.add(0,mActionBean);
            changeFalse();
            PrefUtils.saveObject(PrefUtils.ACTION_LIST,lists);
            return isAdd;
        }

        for (int i= 0 ;i<lists.size();i++){
            if (edit.equals(lists.get(i).getName())){
                isAdd = false;
            }
        }
        if (isAdd){
            mActionBean.setName(edit);
            lists.add(0,mActionBean);
            changeFalse();
            PrefUtils.saveObject(PrefUtils.ACTION_LIST,lists);
        }
        return isAdd;
    }

    /**
     * 初始化状态
     */
    private void changeFalse(){
        for (int i =0 ;i< mActionBean.getFrameList().size();i++){
            mActionBean.getFrameList().get(i).setSelect(false);
        }
    }
    private void initData() {
        mActionBean = (ActionBean) getIntent().getSerializableExtra("action");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        toolTop = (Toolbar) findViewById(R.id.tool_top);
        tvName = (TextView) findViewById(R.id.tv_name);
        mEditText = (EditText) findViewById(R.id.edt_combine_name);
        btnSave = (Button) findViewById(R.id.btn_save);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
