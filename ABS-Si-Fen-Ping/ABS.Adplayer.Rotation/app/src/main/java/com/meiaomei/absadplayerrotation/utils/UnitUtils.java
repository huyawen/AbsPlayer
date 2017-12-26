package com.meiaomei.absadplayerrotation.utils;

import android.content.Context;

import com.meiaomei.absadplayerrotation.AbsPlbsApplication;

import java.util.UUID;

/**
 *
 * huyawen
 * Date: 2017/3/3
 * Time: 10:06
 * Dest:  单位工具计算
 */
public class UnitUtils {
    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context paramContext, float paramFloat) {
        return (int) (0.5F + paramFloat * paramContext.getResources().getDisplayMetrics().density);
    }

    public static int dip2px(float paramFloat) {
        return (int) (0.5F + paramFloat * AbsPlbsApplication.getAppContext().getResources().getDisplayMetrics().density);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context paramContext, float paramFloat) {
        return (int) (0.5F + paramFloat / paramContext.getResources().getDisplayMetrics().density);
    }

    public static int px2dip(float paramFloat) {
        return (int) (0.5F + paramFloat * AbsPlbsApplication.getAppContext().getResources().getDisplayMetrics().density);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int px2sp(float pxValue) {
        final float fontScale = AbsPlbsApplication.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int sp2px(float spValue) {
        final float fontScale = AbsPlbsApplication.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 产生32位，删除"-"符合的UUID。
     *
     * @return 适应于本应用的UUID字符串
     */
    public static String generateUuid() {
        String id = "";
        String[] str = UUID.randomUUID().toString().split("-");
        for (int x = 0; x < str.length; x++) {
            id = id + str[x];
        }
        return id;
    }

}
