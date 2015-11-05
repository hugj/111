package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.os.StrictMode;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CompetitionActivity extends AppCompatActivity {

    private String ans;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void init() {
        StrictMode.setThreadPolicy(
                new StrictMode.ThreadPolicy.Builder().
                        detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().
                detectLeakedSqlLiteObjects().detectLeakedClosableObjects().
                penaltyLog().penaltyDeath().build());

        tryGetQue();
    }

    // 尝试获取问题
    private void tryGetQue() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection clientConnection = new Connection();
                try {
                    String que = clientConnection.StartCom();
                    int i = 0;
                    while (que.isEmpty()) {
                        Thread.sleep(1000); // 每1s请求一次返回信息
                        que = clientConnection.getQue();

                        if ((i++) == 99) { // 循环100次后停止尝试
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "连接失败，请检查网络是否连接并重试",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        }
                    }
                    if (!que.isEmpty()) {
                        TextView tvv = (TextView) findViewById(R.id.que);
                        tvv.setText(que);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void Submit(View view) {
        // Do something in response to button
        EditText Edit_ans = (EditText)findViewById(R.id.edit_ans);
        ans = Edit_ans.getText().toString();

        //is null?
        if (ans.isEmpty()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "请输入答案",
                            Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        new Thread(runnable).start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            checkAns();
        }
    };

    private void checkAns() {
//        String jsonStirng = "{" +
//                "\"ans\":\"" + ans  + "\" " +  "}";
//        String message = RequestHandler.sendPostRequest(
//                "http://192.168.1.32:8000/ans/", jsonStirng);
        if (count == 0) {
            Intent intent = new Intent(this, EndActivity.class);
            startActivity(intent);
            CompetitionActivity.this.finish();
        }
    }



}
