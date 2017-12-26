package com.meiaomei.absadplayerrotation.manager;

import android.content.Context;
import com.meiaomei.absadplayerrotation.view.dialog.AlertDialogCommon;


/**
 * Created by meiaomei on 2017/3/13.
 */
public class AlertDialogManager {

    /**
     * 简单的dialog - 无逻辑 1个按钮
     *
     * @param contents 内容
     */
    public static void creatDialogWithOneButtonNoLogicNoTitle(Context context, String[] contents) {
        new AlertDialogCommon.Builder(context).setContents(contents).build().createAlertDialog();
    }

    /**
     * 简单的dialog - 无逻辑 1个按钮
     *
     * @param title    题目
     * @param contents 内容
     */
    public static void creatDialogWithOneButtonTitleNoLogic(Context context, String title, String[] contents) {
        new AlertDialogCommon.Builder(context).setTitle(title).setContents(contents).build().createAlertDialog();
    }

    /**
     * 简单的dialog - 无逻辑 2个按钮
     *
     * @param contents 内容
     */
    public static void creatDialogWithTwoButtonNoLogicNoTitle(Context context, String[] contents) {
        new AlertDialogCommon.Builder(context).setIsShowCancelBtn(true).setContents(contents).build().createAlertDialog();
    }

    /**
     * 简单的dialog - 无逻辑 2个按钮
     *
     * @param title    题目
     * @param contents 内容
     */
    public static void creatDialogWithTwoButtonTitleNoLogic(Context context, String title, String[] contents) {
        new AlertDialogCommon.Builder(context).setIsShowCancelBtn(true).setTitle(title).setContents(contents).build().createAlertDialog();
    }


}
