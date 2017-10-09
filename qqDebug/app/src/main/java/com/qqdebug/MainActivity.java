package com.qqdebug;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.qqdebug.adapter.EditActionListAdapter;
import com.qqdebug.bean.ActionBean;
import com.qqdebug.utils.GsonUtil;
import com.qqdebug.utils.PermissionHelper;
import com.qqdebug.utils.PrefUtils;
import com.ubt.ip.client.api.MotorApi;
import com.ubt.ip.client.util.InputMethod;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import javax.crypto.Mac;

/**
 * 内容调试界面
 */
public class MainActivity extends AppCompatActivity {

    private Button createButton;
    private RecyclerView rvEdit;
    private EditActionListAdapter mListAdapter;
    private EditText mEditText;
    private Button btnSure;
    private Button buttonOutput;
    private Button buttonImport;
    private List<ActionBean> mLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ActionDebugActivity.class));
                InputMethod.closeInputMethod(MainActivity.this);
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String ip =  mEditText.getText().toString().trim();
                if (!ip.equals("")){
                    PrefUtils.saveObject(PrefUtils.URL,ip);
                    MotorApi.get().setIp(ip);
                    Toast.makeText(MainActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断用户是否已经授权，未授权则向用户申请授权，
                if (!PermissionHelper.hasPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    PermissionHelper.requestPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
                }else {
                    List<ActionBean> list = (List<ActionBean>) PrefUtils.readObject(PrefUtils.ACTION_LIST);
                    String path = Environment.getExternalStorageDirectory()+"/action.txt";
                    if (list !=null){
                        Gson gson = new Gson();
                        String string =gson.toJson(list);
                        contentToTxt(path,string);
                    }
                }

            }
        });
        buttonImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionHelper.hasPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    PermissionHelper.requestPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, 1);
                }else {
                    String path = Environment.getExternalStorageDirectory() + "/action.txt";
                    String addContent = importContent(path);
                    List<ActionBean> lists = GsonUtil.fromJson(addContent, new TypeToken<List<ActionBean>>(){});
                    if (lists == null){
                        return;
                    }
                    if (mLists == null) {
                        mLists = new ArrayList<>();
                    }
                    mLists.addAll(lists);
                    mListAdapter.notifyData(mLists);
                    PrefUtils.saveObject(PrefUtils.ACTION_LIST, mLists);
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(MainActivity.this.getClass().getSimpleName(),"permissions=="+permissions[0]);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)){
                String path = Environment.getExternalStorageDirectory() + "/action.txt";
                String addContent = importContent(path);
                List<ActionBean> lists = GsonUtil.fromJson(addContent, new TypeToken<List<ActionBean>>() {
                });
                if (mLists == null) {
                    mLists = new ArrayList<>();
                }
                mLists.addAll(lists);
                mListAdapter.notifyData(mLists);
                PrefUtils.saveObject(PrefUtils.ACTION_LIST, mLists);
            }else if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                List<ActionBean> list = (List<ActionBean>) PrefUtils.readObject(PrefUtils.ACTION_LIST);
                String path = Environment.getExternalStorageDirectory()+"/action.txt";
                Gson gson = new Gson();
                String string =gson.toJson(list);
                contentToTxt(path,string);
            }

        }

    }
    private void initView() {
        createButton = (Button)findViewById(R.id.button);
        rvEdit = (RecyclerView)findViewById(R.id.rv_edit);
        mEditText = (EditText) findViewById(R.id.et_ip);
        btnSure = (Button) findViewById(R.id.btn_sure);
        buttonOutput = (Button) findViewById(R.id.button_output);
        buttonImport = (Button) findViewById(R.id.button_import);
    }

    private void initData(){
        MotorApi.get().setIp((String) PrefUtils.readObject(PrefUtils.URL));
        mEditText.setHint(String.format("设置机器人ip(%s)","当前ip为："+ PrefUtils.readObject(PrefUtils.URL)));
        InputMethod.closeInputMethod(MainActivity.this);
        mLists = (List<ActionBean>) PrefUtils.readObject(PrefUtils.ACTION_LIST);
        mListAdapter = new EditActionListAdapter(mLists,this);
        LinearLayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvEdit.setLayoutManager(lm);
        rvEdit.setAdapter(mListAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        List<ActionBean>  lists = (List<ActionBean>) PrefUtils.readObject(PrefUtils.ACTION_LIST);
        mListAdapter.notifyData(lists);
    }

    private String importContent(String filePath){
        StringBuilder sb = new StringBuilder();
        String rdLine = null;
        FileReader fr = null;
        BufferedReader bufferedReader = null;
        File f = new File(filePath);
        if (!f.exists()){
            Toast.makeText(MainActivity.this,"action.txt 文件不存在！",Toast.LENGTH_SHORT).show();
        }else {
            try {
                 fr = new FileReader(f);
                 bufferedReader = new BufferedReader(fr);
                while ((rdLine= bufferedReader.readLine())!=null){
                    sb.append(rdLine);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (fr!=null){
                    try {
                        fr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bufferedReader!=null){
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sb.toString();
    }
    private   void contentToTxt(String filePath, String content) {
        String str = new String(); //原有txt内容
        String s1 = new String();//内容更新
        try {
            File f = new File(filePath);
            if (f.exists()) {
                System.out.print("文件存在");
            } else {
                System.out.print("文件不存在");
                f.createNewFile();// 不存在则创建
            }
            s1 += content;
            BufferedWriter output = new BufferedWriter(new FileWriter(f));
            output.write(s1);
            output.close();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this,"写入成功",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
