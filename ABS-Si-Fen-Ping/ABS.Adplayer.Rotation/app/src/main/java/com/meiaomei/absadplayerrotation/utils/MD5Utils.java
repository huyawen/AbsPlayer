package com.meiaomei.absadplayerrotation.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/6/16.
 */
public class MD5Utils {


    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f' };
    protected static MessageDigest messagedigest = null;
    static{
        try{
            messagedigest = MessageDigest.getInstance("MD5");
        }catch(NoSuchAlgorithmException nsaex){
            System.err.println(MD5Utils.class.getName()+"初始化失败,MessageDigest不支持MD5Utils。");
            nsaex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String md5=getFileMD5(new File(""));
        System.out.println("md5:"+md5);
    }


    public static String getFileMD5(File file) throws IOException {
//	   FileLock lock=null;
        FileChannel fc=null;
        String md5="";
        try {
            fc=new RandomAccessFile(file,"rw").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            messagedigest.update(byteBuffer);
            byte [] b=messagedigest.digest();
            md5=bufferToHex(b);
            return md5;
        }catch (IOException e) {
            throw new IOException("取MD5异常");
        }
        finally{
            if(fc!=null){
                fc.close();
            }
        }
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }


    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }


    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
