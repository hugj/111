package com.example.administrator.myapplication;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
/**
 * Created by Administrator on 2015/11/4.
 */
public class Connection {
    private Socket socket;

    private InputStream is;

    private OutputStream os;

    private String msg = null;

    public Connection()
    {
        try {
            socket = new Socket("192.168.1.11", 8888);
            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String login(final String user)
    {
        try {
            if (!user.isEmpty()) {
                // 向服务器端发送用户的登录信息
//                os.write(user.getBytes());
//                os.flush();

                PrintStream out = new PrintStream(socket.getOutputStream(),true,"UTF-8");
                out.print("hello server!");
                out.flush();
            }
            //获取返回信息
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            msg = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return msg;
    }
}
