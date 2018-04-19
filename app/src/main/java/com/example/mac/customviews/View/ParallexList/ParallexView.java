package com.example.mac.customviews.View.ParallexList;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by mac on 2018/4/19.
 */

public class ParallexView extends ListView{

    private ImageView imageViews;
    private int orignHeight;
    private int imageHeight;
    private int maxHeight;

    public ParallexView(Context context) {
        super(context);
    }

    public ParallexView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setParallexImageView(final ImageView imageView1) {
        this.imageViews = imageView1;
        imageViews.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //在viewrootimp的performlayout后执行 能获取长宽
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {//mLayoutRequested为true导致触发onGlobalLayout

                imageViews.getViewTreeObserver().removeOnGlobalLayoutListener(this);//只执行一次
                orignHeight=imageViews.getHeight();//在这个树是layout之后再获取height
                imageHeight=imageViews.getDrawable().getIntrinsicHeight();//得到真实图片的高度
                maxHeight=orignHeight>imageHeight?orignHeight*2:imageHeight;
                Log.e("xxx",orignHeight+"&&"+imageHeight);
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {


        switch (ev.getAction()){

            case MotionEvent.ACTION_UP://回弹
                //最后的回弹效果
                //属性从 手收回时图片高度-》我们设置的高度
                ValueAnimator valueAnimator=ValueAnimator.ofInt(imageViews.getHeight(),orignHeight);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value= (int) animation.getAnimatedValue();//获取动画的值
                        imageViews.getLayoutParams().height=value;//将动画的值赋值给图片的布局参数
                        imageViews.requestLayout();//使imageview的布局参数生效 （类型重绘-只不过是重新布局）
                    }
                });
                valueAnimator.setInterpolator(new OvershootInterpolator());//动画插值器 弹性
                valueAnimator.setDuration(500);
                valueAnimator.start();
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * listview滑动到头执行，可以继续获取滑动的距离和方向
     * @param deltaX  继续滑动X方向的距离
     * @param deltaY  继续滑动Y方向的距离   负：顶部到头 正：底部到头
     * @param scrollX  当前的mScrollX和mScrollY的值。
     * @param scrollY
     * @param scrollRangeX 标示可以滚动的最大的x,y值 也就是你视图真实的长和宽
     * @param scrollRangeY
     * @param maxOverScrollX  X方向最大可以滚动的距离
     * @param maxOverScrollY  Y方向最大可以滚动的距离
     * @param isTouchEvent    true 是手指拖动滑动 false惯性滑动 fliding
     * @return
     */


    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        Log.e("xxx","overScrollBy");
        if(imageViews!=null){
            if(deltaY<0&&isTouchEvent){//不断增加imageview的高度
                int newheight=imageViews.getHeight()-deltaY/3;//deltaY为负 加上我滑动距离1/3的高度
                if(newheight>maxHeight){//控制高度
                    newheight=maxHeight;
                }
                imageViews.getLayoutParams().height=newheight;
                imageViews.requestLayout();//使imageview的布局参数生效
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }
}
