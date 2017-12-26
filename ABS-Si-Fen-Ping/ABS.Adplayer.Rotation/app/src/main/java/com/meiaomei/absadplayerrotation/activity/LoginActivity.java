package com.meiaomei.absadplayerrotation.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.meiaomei.absadplayerrotation.R;
import com.meiaomei.absadplayerrotation.model.bean.RegistrationItemModel;
import com.meiaomei.absadplayerrotation.utils.DateUtils;
import com.meiaomei.absadplayerrotation.utils.FileUtils;
import com.meiaomei.absadplayerrotation.utils.ScreenUtils;
import com.meiaomei.absadplayerrotation.utils.SharedPrefsUtil;
import com.meiaomei.absadplayerrotation.utils.ShowHomeImgDialog;
import com.meiaomei.absadplayerrotation.utils.SoftwareUtils;
import com.meiaomei.absadplayerrotation.view.ZToast;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class LoginActivity extends Activity {

    @ViewInject(R.id.ed_Name)
    EditText ed_Name;
    @ViewInject(R.id.ed_Num)
    EditText ed_Num;
    @ViewInject(R.id.bt_done1)
    Button bt_done1;
    @ViewInject(R.id.bt_done2)
    Button bt_done2;
    @ViewInject(R.id.bt_cancle2)
    Button bt_cancle2;
    @ViewInject(R.id.img_zxing)
    ImageView img_zxing;

    @ViewInject(R.id.tv_2)
    TextView tv_2;

    Build bd;
    String dateS;
    File imgFilePath;
    String sha1;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);

        //删除解压缩过的apk包以及Apk文件夹
        if (FileUtils.isFileExist("Apk")&&FileUtils.isDirectory("Apk")){
            FileUtils.deleteAllFiles(new File(FileUtils.SDPATH+"Apk"));
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 23 && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//android6.0需要动态申请权限
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);//发起申请读写文件的权限
            }
        }

        bd = new Build();
        gson = new Gson();
        bt_done1.setOnClickListener(new MyBtnListener());
        bt_done2.setOnClickListener(new MyBtnListener());
        bt_cancle2.setOnClickListener(new MyBtnListener());
        checkSha1Value();
    }

    //检查本地的sha1值和服务器返回的sha1值是否相等
    private void checkSha1Value() {
        String webSha1 = SharedPrefsUtil.getValue(LoginActivity.this, "webSha1", "webSha1");
        String userName = SharedPrefsUtil.getValue(LoginActivity.this, "userName", "userName");
        String asseblyName = SharedPrefsUtil.getValue(LoginActivity.this, "asseblyName", "asseblyName");
        String asseblyVersion = SharedPrefsUtil.getValue(LoginActivity.this, "asseblyVersion", "asseblyVersion");
        String cpuId=bd.CPU_ABI;
        RegistrationItemModel ri = new RegistrationItemModel();
        ri.setAssemblyTitle(asseblyName);
        ri.setCusterm(userName);
        ri.setCpuID(cpuId);
        ri.setAssemblyVersion(asseblyVersion);
        String  riJson=gson.toJson(ri);
        String localSha1 = GetDigest(riJson);
        if (localSha1.equals(webSha1)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LoginActivity.this, SetActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).start();
        } else {
            return;
        }
    }


    class MyBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_done1:
                    String userName = ed_Name.getText().toString().replaceAll(" ", "");
                    if (!TextUtils.isEmpty(userName)) {//用户名不为空的时候生成二维码
                        sha1 = DeviceActivationCode(userName);
                        ShowHomeImgDialog.ShowHomeImg(LoginActivity.this, imgFilePath.getAbsolutePath());
                        tv_2.setVisibility(View.VISIBLE);
                        ed_Num.setVisibility(View.VISIBLE);
                        bt_done2.setVisibility(View.VISIBLE);
                        bt_cancle2.setVisibility(View.VISIBLE);
                    } else {
                        ZToast.getInstance(LoginActivity.this).show("请输入机构名称！", Toast.LENGTH_SHORT);
                    }
                    break;

                case R.id.bt_done2://生成的秘钥和给我的一样就进入设置页面
                    String ed_text = ed_Num.getText().toString().replaceAll(" ", "");
                    if (ed_text.equals(sha1)) {//满足sha1，给出成功的提示
                        ZToast.getInstance(LoginActivity.this).show("设备已成功激活", Toast.LENGTH_SHORT);
                        SharedPrefsUtil.putValue(LoginActivity.this, "webSha1", ed_text);//将设备生成的sha1存在sp里
                        Intent intent = new Intent(LoginActivity.this, SetActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ZToast.getInstance(LoginActivity.this).show("激活码无效", Toast.LENGTH_SHORT);
                    }
                    break;

                case R.id.bt_cancle2:
                    finish();
                    break;
            }
        }
    }

    //根据四个参数得到设备的 sha1加密码
    public String DeviceActivationCode(String userName) {
        //创建registCode
        RegistrationItemModel ri = new RegistrationItemModel();
        String cpuId = bd.CPU_ABI;
        String asseblyName = "Adplayer.Scroll.A";
        String asseblyVersion = String.valueOf(SoftwareUtils.getVersionCode(LoginActivity.this));
        ri.setAssemblyTitle(asseblyName);//得到app名称
        ri.setAssemblyVersion(asseblyVersion);//得到app版本号  versionCode这个号是程序内部识别的版本
        ri.setCpuID(cpuId);//bd 得到cpu的id
        ri.setCusterm(userName);//机构名称（即用户名称）
        SharedPrefsUtil.putValue(LoginActivity.this, "userName", userName);//将username存在sp里
        SharedPrefsUtil.putValue(LoginActivity.this, "asseblyName", asseblyName);//将asseblyName存在sp里
        SharedPrefsUtil.putValue(LoginActivity.this, "asseblyVersion", asseblyVersion);//将asseblyVersion存在sp里

        XStream xStream = new XStream();
        xStream.processAnnotations(RegistrationItemModel.class);
        String riXmlString = xStream.toXML(ri);
        String jsonaS=gson.toJson(ri);
        String RegistrationCode = Encrypt(jsonaS, "npiTUAL6InCrYPLA++dbtlQfnqCNoVG4", "SqHzP3eZlXE=");
        Date date = new Date();
        dateS = DateUtils.formatDate(date, "yyyy-MM-dd");
        imgFilePath = new File(FileUtils.SDPATH + userName + "-" + dateS + ".png");
        boolean success = ScreenUtils.createQRImage(RegistrationCode, 800, 800,
                null,
                imgFilePath.getAbsolutePath());
        if (success) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    img_zxing.setImageBitmap(BitmapFactory.decodeFile(imgFilePath.getAbsolutePath()));
                }
            });
        }

        String riJson = gson.toJson(ri);
        return GetDigest(riJson);
    }

    //将设备的四个值转为json串通过sha1加密
    public String GetDigest(String jsonString) {
        String str = "";
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("sha-1");
            byte[] byt = jsonString.getBytes("UTF-16LE");
            m.update(byt);
            byte diByte[] = m.digest();
            str = bytes2String(diByte);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        str = str.substring(8, 24);
        return str;
    }

    private String bytes2String(byte[] aa) {//将字节数组转换为字符串
        String hash = "";
        for (int i = 0; i < aa.length; i++) {//循环数组
            int temp;
            if (aa[i] < 0)           //判断是否是负数
                temp = 256 + aa[i];
            else
                temp = aa[i];
            if (temp < 16)
                hash += "0";
            hash += Integer.toString(temp, 16);//转换为16进
        }
        hash = hash.toUpperCase();              //转换为大写
        return hash;
    }

    //加密设备信息
    public  String Encrypt(String content, String strKey, String strIV) {//key值和iv值用64位转 内容不用64位转（坑）
        byte[] bOut = null;
        // 把字符串明文转换成utf-8编码的字节流
        byte[] data = new byte[0];
        try {
            data = content.getBytes("UTF-8");
            Key deskey = null;
            byte[] sKey = new BASE64Decoder().decodeBuffer(strKey);
            DESedeKeySpec spec = new DESedeKeySpec(sKey);
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");//PKCS7Padding  desede"+"/CBC/ZeroPadding
            BASE64Decoder d = new BASE64Decoder();
            byte[] b = d.decodeBuffer(strIV);
            IvParameterSpec ips = new IvParameterSpec(b);
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);//ENCRYPT_MODE,加密数据   DECRYPT_MODE,解密数据
            bOut = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        String str = encoder.encode(bOut);
        return str;
    }

    //解密得到的数据
    public  String deCrypt(String content, String strKey, String strIV){
        BASE64Decoder decoder=new BASE64Decoder();
        String s="";
        try {
            byte [] deContent= decoder.decodeBuffer(content);
            byte [] ivByte=decoder.decodeBuffer(strIV);
            byte[] sKey = new BASE64Decoder().decodeBuffer(strKey);
            DESedeKeySpec spec = new DESedeKeySpec(sKey);
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            Key deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");//PKCS7Padding  desede"+"/CBC/ZeroPadding
            IvParameterSpec ips = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);//ENCRYPT_MODE,加密数据   DECRYPT_MODE,解密数据
            byte [] bOut = cipher.doFinal(deContent);
            s=new String(bOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {//edittext得到焦点 隐藏
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //权限请求的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean writeAccepted = false;
        switch (requestCode) {
            case 1://动态请求的第一个权限
                writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (writeAccepted) {
                    break;
                } else {
                    Toast.makeText(this, "请授权，我们需要这个权限", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);//发起申请读写文件的权限
                }
                break;
        }
    }
}
