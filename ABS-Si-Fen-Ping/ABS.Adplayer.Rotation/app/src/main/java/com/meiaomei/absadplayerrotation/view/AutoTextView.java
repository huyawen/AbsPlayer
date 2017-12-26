package com.meiaomei.absadplayerrotation.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by meiaomei on 2017/4/21.
 */
public class AutoTextView extends TextView {

    public static final String TAG = "AutoTextView";
    public PlayCntListener playCntListener;
    int cnt = 0;

    public void setPlayCntListener(PlayCntListener playCntListener) {
        this.playCntListener = playCntListener;
    }

    /**
     * 字幕滚动的速度 快，普通，慢
     */
    public static final int SCROLL_SLOW = 0;
    public static final int SCROLL_NORM = 1;
    public static final int SCROLL_FAST = 2;

    /**
     * 字幕内容
     */
    private String mText;

    /**
     * 字幕字体颜色
     */
    private int mTextColor;

    /**
     * 字幕字体大小
     */
    private float mTextSize;

    private float offX = 0f;

    private float mStep = 0.5f;

    private Rect mRect = new Rect();

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public AutoTextView(Context context) {
        super(context);
        setSingleLine(true);
    }

    public AutoTextView(Context context, AttributeSet attr) {
        super(context, attr);
        setSingleLine(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mText = getText().toString();
        mTextColor = getCurrentTextColor();
        mTextSize = getTextSize();
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mText, 0, mText.length(), mRect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x, y;
        // offX 为横坐标偏移，一开始偏移为0，逐渐增大
        x = getMeasuredWidth() - offX;
        y = getMeasuredHeight() / 2 + (-mPaint.ascent() - mPaint.descent()) / 2+1;
        canvas.drawText(mText, x, y, mPaint);
        // 增加横坐标偏移，左移绘制起点 ； mStep为偏移步伐
        offX += mStep;
        // 当偏移大于getMeasuredWidth() + mRect.width()时，说字符串尾巴都在View之外，归零偏移量
        if (offX >= getMeasuredWidth() + mRect.width()) {
            offX = 0f;
            cnt++;//记录播放次数
            playCntListener.getPlayCnt(cnt);
        }
        invalidate();
    }

    /**
     * 设置字幕滚动的速度
     */
    public void setScrollMode(int scrollMod) {
        if (scrollMod == SCROLL_SLOW) {
            mStep = 0.5f;
        } else if (scrollMod == SCROLL_NORM) {
            mStep = 1f;
        } else if (scrollMod == SCROLL_FAST) {
            mStep = 1.5f;
        } else {
            mStep = 2.0f;
        }
    }

    public interface PlayCntListener {
        void getPlayCnt(int cnt);
    }

}
