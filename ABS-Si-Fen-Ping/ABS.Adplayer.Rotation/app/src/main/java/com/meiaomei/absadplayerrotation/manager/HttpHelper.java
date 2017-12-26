package com.meiaomei.absadplayerrotation.manager;

import android.util.Log;

import com.meiaomei.absadplayerrotation.utils.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by meiaomei on 2017/3/2.
 */
public class HttpHelper {

    private URL url = null;//00-5A-18-92-71-ED       5A-E4-C5-EE-50-03
//    http://128.160.97.6:16300/zhyh//ecpweb/page/zhyh/Rate/save.jsp?show=1 网页去掉zhyh//
    public static final String SERVER_URL_TEST_CHENGDU = "http://128.160.97.6:16300/zhyh/PlayDev";//心跳上报--不用加/
    public static final String SERVER_URL_TEST_DOWN = "http://128.160.97.6:16300/zhyh/";//下载 --zhyh/ 一定要加/
    public static final String SERVER_URL = "http://192.168.2.136/abs/";
    public static final String SERVER_URL_RELASE = "http://28.0.160.68:16300/zhyh/PlayDev";
    public static final String SERVER_URL_TEST = "http://192.168.2.102/ABS/";
    public static final String SERVER_URL_SYNC = "http://192.168.2.100:8080/http/playlistsync";//资源
    private static StringBuffer postParams = new StringBuffer();//请求参数，请求参数应该是 name1=value1&name2=value2 的形式


    /**
     * 读取文本文件
     *
     * @param urlStr url路径
     * @return 文本信息
     * 根据url下载文件，前提是这个文件中的内容是文本
     * 1.创建一个URL对象
     * 2.通过URL对象，创建一个Http连接
     * 3.得到InputStream
     * 4.从InputStream中得到数据
     */
    public String download(String urlStr) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader bufferedReader = null;

        try {
            url = new URL(urlStr);
            //创建http连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            //使用IO流读取数据
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("TAG", "下载txt文件");
        Log.e("TAG", sb.toString());
        return sb.toString();
    }


    /**
     * 读取任何文件
     * 返回-1 ，代表下载失败。返回0，代表成功。返回1代表文件已经存在
     *
     * @param urlStr
     * @param path
     * @param fileName
     * @return
     */
    public int downlaodFile(String urlStr, String path, String fileName,String md5) {
        InputStream input = null;
        try {
            url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(1 * 60 * 1000);//设置连接超时时间
            urlConn.setReadTimeout(1 * 60 * 1000);// 设置读取数据的超时时间
            urlConn.connect();
            int fileLength = urlConn.getContentLength();
            input = urlConn.getInputStream();
            FileUtils fileUtil = new FileUtils();
            if (fileUtil.isFileExist(path + fileName) && fileUtil.isFileExistbyMD5(path + fileName, md5)) {//文件名相同且md5相同 认为一个文件
                return 1;//返回1 文件已存在，不重复下载
            }else {
                File resultFile = fileUtil.write2SDFromInput(path, fileName, input, fileLength, null);//path   mate  filename  test
                if (resultFile == null)
                    return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }


    /**
     * 读取视频文件
     * 返回-1 ，代表下载失败。返回0，代表成功。返回1代表文件已经存在
     *
     * @param urlStr
     * @param path
     * @param fileName
     * @return
     */
    public int downloadVideo(String urlStr, String path, String fileName, FileUtils.ProgressListener listener, String md5) {
        InputStream input = null;
        try {
            url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(1 * 60 * 1000);// 设置连接超时时间
            urlConn.setReadTimeout(1 * 60 * 1000);// 设置读取数据的超时时间
            urlConn.connect();
            int fileLength = urlConn.getContentLength();
            input = urlConn.getInputStream();
            FileUtils fileUtil = new FileUtils();
            if (fileUtil.isFileExist(path + fileName) && fileUtil.isFileExistbyMD5(path + fileName, md5)) {//文件名相同且md5相同 认为一个文件
                return 1;//返回1 文件已存在，不重复下载
            } else {
                File resultFile = fileUtil.write2SDFromInput(path, fileName, input, fileLength, listener);//path   mate  filename  test
                if (resultFile == null)
                    return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }


    public InputStream getInputStearmFormUrl(String urlStr) throws IOException {
        InputStream input = null;
        url = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setConnectTimeout(1 * 60 * 1000);// 设置连接超时时间
        urlConn.setReadTimeout(1 * 60 * 1000);// 设置读取数据的超时时间
        urlConn.connect();
        urlConn.getContentLength();
        input = urlConn.getInputStream();
        return input;
    }

    /**
     * Get请求，获得返回数据
     *
     * @param urlStr
     * @return
     * @throws Exception
     */
    public static void doGet(String urlStr, int connectionTimeout, HttpSuccessResponse responseInfo, HttpFaliureResponse faliureResponse) {
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(postParams.toString().length() == 0 ? urlStr : urlStr + "&" + postParams.toString());//1.设置URL以及参数
            conn = (HttpURLConnection) url.openConnection();//2.url连接打开
            conn.setRequestMethod("GET"); // 3:设置请求的方式
            // 4:其他设置
            conn.setDoInput(true);// 是否允许从服务器读取数据
            conn.setDoOutput(false);// 是否允许向服务器写入数据
            conn.setRequestProperty("contentType", "UTF-8");
            conn.setConnectTimeout(connectionTimeout * 1000);// 设置连接超时时间
            conn.setReadTimeout(connectionTimeout * 1000);// 设置读取数据的超时时间
            conn.connect();
            // 5:获取响应码
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                responseInfo.successCallBack(conn.getInputStream());
            } else {
                faliureResponse.getFalureConnectionCode(code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                postParams.delete(0, postParams.length());
                if (conn != null) conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     * @throws Exception
     */
    public static void doPost(String url, int connectionTimeout, HttpSuccessResponse responseInfo, HttpFaliureResponse faliureResponse) {
        PrintWriter out = null;
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(connectionTimeout * 1000);
            conn.setConnectTimeout(connectionTimeout * 1000);
            String param = postParams.toString();
            if (!param.trim().equals("")) {
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(param);
                // flush输出流的缓冲
                out.flush();
            }
            conn.connect();
            // 5:获取响应码
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                responseInfo.successCallBack(conn.getInputStream());
            } else {
                faliureResponse.getFalureConnectionCode(code);
            }
        }
        // 使用finally块来关闭输出流、输入流
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                postParams.delete(0, postParams.length());
                if (out != null) {
                    out.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public HttpHelper setRequestParams(String key, String value) {
        if (postParams.length() > 0) {
            postParams.append("&" + key + "=" + value);
        } else {
            postParams.append("&" + key + "=" + value);
        }
        return this;
    }

    public interface HttpSuccessResponse {
        public void successCallBack(InputStream inputStream);
    }

    public interface HttpFaliureResponse {
        public void getFalureConnectionCode(int code);
    }

}
