package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {

    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void clicklogin(View view) {
//        Socket socket = null;
//        try {
//            socket = new Socket("192.168.1.11", 8888);
//            //向服务器传消息
//            PrintStream out = new PrintStream(socket.getOutputStream(),true,"UTF-8");
//            out.print("hello server!");
//            out.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        EditText Edit_user = (EditText)findViewById(R.id.edit_user);
        user = Edit_user.getText().toString();

        //is null?
        if (user.isEmpty()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "请输入用户名",
                            Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        login();
    }

    private void login() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Connection clientConnection =  (Connection) LoginActivity.this.getApplication();
                try {
                    clientConnection.init();

                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                //Connection clientConnection = new Connection();
                try {
                    //判断是否登录成功
                    String msg = clientConnection.login(user);
                    int i = 0;
                    while (msg.isEmpty()) {
                        Thread.sleep(1000); // 每1s请求一次登陆返回信息
                        msg = clientConnection.getMsg();

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
                    if (!msg.isEmpty()) {
                        //clientConnection.close();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
