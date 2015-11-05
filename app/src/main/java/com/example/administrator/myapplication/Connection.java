package com.example.administrator.myapplication;

import android.app.Application;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
/**
 * Created by Administrator on 2015/11/4.
 */
public class Connection extends Application {
    private Socket socket;

    private InputStream is;

    private OutputStream os;

    private String msg = null;

    public void init() throws IOException, Exception{
        this.socket = new Socket("192.168.1.16", 8888);
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();
    }

    public void close()
    {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String login(final String user)
    {
        try {
            // 向服务器端发送用户的登录信息
            PrintStream out = new PrintStream(socket.getOutputStream(),true,"UTF-8");
            out.println(user);// 写到服务器
            //out.print(user + "\n");
            out.flush();

            //获取返回信息
            msg = getMsg();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public String getMsg()
    {
        try {
            //获取返回信息
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            msg = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public String StartCom()
    {
        try {
            String msg = "start";
            PrintStream out = new PrintStream(socket.getOutputStream(),true,"UTF-8");
            out.println(msg);// 写到服务器
            //out.print(msg + "\n");
            out.flush();

            //获取返回信息
            msg = getMsg();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public String submitAns(final String ans)
    {
        try {
            PrintStream out = new PrintStream(socket.getOutputStream(),true,"UTF-8");
            out.println(ans);// 写到服务器
            //out.print(msg + "\n");
            out.flush();

            //获取返回信息
            msg = getMsg();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
