package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

public class MatchActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);

        rippleBackground.startRippleAnimation();
        tryMatch();
    }

    // 尝试匹配
    private void tryMatch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean flag = true;
                    int i = 0;

                    do {
                        //if (!flag)
                            Thread.sleep(5000); // 每5s发送一次匹配信息
                        flag = matching();
                        if ((i++) == 9) { // 循环10次后停止匹配的尝试
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "还没有人参加比赛，少侠请稍后再试",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                            Thread.sleep(1000);
                            Intent intent = new Intent(MatchActivity.this, MainActivity.class);
                            startActivity(intent);
                            MatchActivity.this.finish();
                        }
                    } while (!flag);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //匹配
    private Boolean  matching() {
        Intent intent = new Intent(this, CompetitionActivity.class);
        startActivity(intent);
        MatchActivity.this.finish();
        return true;
    }
}
