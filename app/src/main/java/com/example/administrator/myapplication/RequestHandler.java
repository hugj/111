package com.example.administrator.myapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by Administrator on 2015/11/2.
 */
public class RequestHandler {
    /**
     * 获取指定URL的响应字符串
     * @param urlString
     * @return
     */
    public static String getURLResponse(String urlString){
        HttpURLConnection conn = null; //连接对象
        InputStream is = null;
        String resultData = "";
        try {
            URL url = new URL(urlString); //URL对象
            conn = (HttpURLConnection)url.openConnection(); //使用URL打开一个链接
            conn.setDoInput(true); //允许输入流，即允许下载
            conn.setDoOutput(true); //允许输出流，即允许上传
            conn.setUseCaches(false); //不使用缓冲
            conn.setRequestMethod("GET"); //使用get请求
            is = conn.getInputStream();   //获取输入流，此时才真正建立链接
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine  = "";
            while((inputLine = bufferReader.readLine()) != null){
                resultData += inputLine + "\n";
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(conn != null){
                conn.disconnect();
            }
        }

        return resultData;
    }

    public static String sendGetRequest(String urlString){

        try {

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true); // permit to use the inputstream
            conn.setDoOutput(false); // permit to use the outputstrem
            conn.setUseCaches(false); // deny to use the cache

            StringBuffer sBuffer = new StringBuffer();
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                String line = null;
                InputStream in = conn.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(in));
                while((line = bReader.readLine()) != null) {
                    sBuffer.append(line);
                }
                return sBuffer.toString();
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return "false";
    }

    /* send the request by POST method
     * @param a url path
     * 		  for the ehelp project, must provide the url path
     *        like "http://120.24.208.130:1501/account/login..."
     *        a String with json format
     * @return a string that the server respond with json format
     * 		   a string "false"/null indicate some errors happened
     */
    public static String sendPostRequest(String urlString, String jsondata){

        try {

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true); // permit to use the inputstream
            conn.setDoOutput(true); // permit to use the outputstrem
            conn.setUseCaches(false); // deny to use the cache
            conn.setRequestProperty("Content-Type", "application/json");// set the request Content-Type

            byte data[] = jsondata.getBytes("UTF-8"); // use utf-8 coding format to transformat string to a byte array
            conn.getOutputStream().write(data);

            StringBuffer sBuffer = new StringBuffer();
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                String line = null;
                InputStream in = conn.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(in));
                while((line = bReader.readLine()) != null) {
                    sBuffer.append(line);
                }
                return sBuffer.toString();
            }

        } catch (Exception e) {

            return e.toString();

        }

        return "false";

    }

	/* send a file(.jpg) to server by POST method
	 * @params a url path
	 * 		   a image file
	 * @return a String "true" if upload successfully,0
	 * 		   "false" otherwise
	 */

    public static String uploadFile(String urlString, File file) {

        String PREFIX = "--";
        String LINE_END = "\r\n";
        String BOUNDARY = UUID.randomUUID().toString(); // set the boundary


        try {

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(50000);
            conn.setReadTimeout(50000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true); // permit to use the inputstream
            conn.setDoOutput(true); // permit to use the outputstrem
            conn.setUseCaches(false); // deny to use the cache
            conn.setRequestProperty("Charset", "utf-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data"+";boundary="
                    + BOUNDARY);// set the request Content-Type
            conn.setRequestProperty("connection", "keep-alive");

            if(file != null) {
                OutputStream outputStream = conn.getOutputStream();
                DataOutputStream doStream = new DataOutputStream(outputStream);

                // request's entity body
                StringBuffer sBuffer = new StringBuffer();
                sBuffer.append(PREFIX);
                sBuffer.append(BOUNDARY);
                sBuffer.append(LINE_END);
                sBuffer.append("Content-Disposition: form-data; name=\"avatar\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sBuffer.append("Content-Type: application/octet-stream; charset = utf-8" + LINE_END);
                sBuffer.append(LINE_END);

                doStream.write(sBuffer.toString().getBytes());

                InputStream in = new FileInputStream(file);
                byte[] bytes = new byte[2014];
                int len = 0;
                while((len = in.read(bytes)) != -1) {
                    doStream.write(bytes, 0, len);
                }
                in.close();
                doStream.write(LINE_END.getBytes());

                doStream.write(
                        (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes());
                doStream.flush();

                StringBuffer resBuffer = new StringBuffer();
                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    String line = null;
                    InputStream resin = conn.getInputStream();
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(resin));
                    while((line = bReader.readLine()) != null) {
                        resBuffer.append(line);
                    }
                    return resBuffer.toString();
                }

            }


        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return "false";

    }

    /*
    * Test the validation of url
    * @param urlString, the path of url
    * @return True if connected successfully
    *         False if fails
    */
    public static boolean TestGetURL(String urlString){

        try {

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(500);
            conn.setRequestMethod("GET");
            conn.setDoInput(true); // permit to use the inputstream
            conn.setDoOutput(false); // permit to use the outputstrem
            conn.setUseCaches(false); // deny to use the cache
            int a = conn.getResponseCode();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){

                return true;

            }

        } catch (Exception e) {

            e.printStackTrace();
            String sss = e.toString();
            String a = sss;
            Log.v("123123123123", a);

        }

        return false;
    }
}
