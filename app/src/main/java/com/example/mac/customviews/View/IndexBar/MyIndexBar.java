package com.example.mac.customviews.View.IndexBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.mac.customviews.R;

/**
 * Created by mac on 2018/4/17.
 */

public class MyIndexBar extends View {
    private int width;
    private String[] indexArr = { "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z" };
    private float cellheight;
    private Context mContext;
    private Paint mpaint;
    private float letter_size;
    private static final float DEFAULT_LETTER_SIZE=30;//PX

    public MyIndexBar(Context context) {
        super(context);
        initview();
    }

    public MyIndexBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initAttrs(attrs);
        initview();
    }

    public MyIndexBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        initAttrs(attrs);
        initview();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.MyIndexBar);
        letter_size=typedArray.getDimension(R.styleable.MyIndexBar_letter_size,DEFAULT_LETTER_SIZE);
        Log.e("xxx",letter_size+"!!!");
    }


    private void initview() {
        mpaint=new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿参数
        mpaint.setColor(Color.BLACK);
        mpaint.setTextSize(letter_size);
        mpaint.setTextAlign(Paint.Align.CENTER);
    }


    //onSizeChanged方法一般是视图大小发生变化的时候回调了
    //layout里的setFrame的sizechange方法中的回调 所以发生在mesaure之后


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();//getMeasuredWidth()获取的是view原始的大小，也就是这个view在XML文件中配置或者是代码中设置的大小
        Log.e("tag",width+"xxx");
        cellheight = getMeasuredHeight()*1f/26;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float x=width/2;
        for (int i=0;i<indexArr.length;i++){
            float textHeight= getTextHeight(indexArr[i]);//获取一个字母的高度
            float y=cellheight/2+textHeight /2+i*cellheight;
            mpaint.setColor(lastposition==i?Color.WHITE:Color.BLACK);
            canvas.drawText(indexArr[i],x,y,mpaint);//预览就能看
        }


    }



    private float getTextHeight(String text) {
        //获取文本高度 rect矩形
        Rect bounds=new Rect();
        mpaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }




    //处理事件
    private int position;
    private  int lastposition=-1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                this.setBackgroundColor(Color.TRANSPARENT);
                lastposition=-1;//手抬起重置
                break;
            case MotionEvent.ACTION_DOWN:
                this.setBackgroundColor(Color.BLACK);
                getBackground().setAlpha(80);
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                //getX()是表示Widget相对于自身左上角的x坐标,而getRawX()是表示相对于屏幕左上角的x坐标值(注意:这个屏幕左上角是手机屏幕左上角,不管activity是否有titleBar或是否全屏幕)
                position = (int) (y/cellheight);
                if(position>=0&&position<indexArr.length){
                    if(lastposition!=position){//不重复
                        if(listener!=null){
                            listener.onTouchLetter(indexArr[position]);
                        }
                    }
                }
                lastposition= position;
                break;
        }

        invalidate();
        return true;//消费
    }



    public void setonTouchListener(onTouchLetterListener listener) {
        this.listener = listener;
    }

    private onTouchLetterListener listener;


    public  interface onTouchLetterListener{
        void onTouchLetter(String letter);
    }



}
