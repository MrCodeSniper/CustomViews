package com.example.mac.customviews.View.View_confirm;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.mac.customviews.R;

/**
 * Created by mac on 2018/4/16.
 */

public class ConfirmView extends View {

    private static final float DEFAULT_RADIUS=200;
    private Context mContext;
    private RateEnum defaultRateEnum;

    private float mRadius;
    private int  alphaCount =0;
    private int uncheck_base_color;
    private int check_base_color;
    private int check_tick_color;
    private int progressCount=0;
    private int backRadius=0;
    int centerX=500;
    int centerY=500;
    float tickRadiusOffset;
    private float ringStrokeWidth=0;
    //勾
    Path path;
    //画笔
    Paint paint;
    private boolean isChecked;
    private AnimatorSet mFinalAnimatorSet;
    //圆的外边框
    private RectF mRectF;

    public ConfirmView(Context context) {
        super(context);
        mContext=context;
        init();
        setUpEvent();
    }

    public ConfirmView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initAttrs(attrs);
        init();
        setUpEvent();
    }

    public ConfirmView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        initAttrs(attrs);
        init();
        setUpEvent();
    }


    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ConfirmView);
        //获取配置的动画速度 1
        int rateMode = typedArray.getInt(R.styleable.ConfirmView_rate, RateEnum.RATE_MODE_NORMAL);
        defaultRateEnum = RateEnum.getRateEnum(rateMode);
        mRadius=typedArray.getDimension(R.styleable.ConfirmView_radius,DEFAULT_RADIUS);
        uncheck_base_color=typedArray.getColor(R.styleable.ConfirmView_uncheck_base_color, Color.GRAY);
        check_base_color=typedArray.getColor(R.styleable.ConfirmView_check_base_color, Color.YELLOW);
        check_tick_color=typedArray.getColor(R.styleable.ConfirmView_check_tick_color, Color.WHITE);
        typedArray.recycle();
    }



    private void init(){
        isChecked=false;
        mRectF=new RectF();
        paint=new Paint();
        path=new Path();
        initAnim();
    }


    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                mySize = defaultSize;
                break;
            case MeasureSpec.EXACTLY:
                mySize = size;
                break;
            default:
                break;
        }
        return mySize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("xxx","onMeasure");

        //控件的宽度等于动画最后的扩大范围的半径
        //根据规格画方框
        int width = getMySize((int) ((mRadius + DisplayUtils.dp2px(mContext,50)) * 2), widthMeasureSpec);
        int height = getMySize((int) ((mRadius + DisplayUtils.dp2px(mContext,50)) * 2), heightMeasureSpec);

        height = width = Math.max(width, height);

        setMeasuredDimension(width, height);//设置控件方框的宽高

        //取方框中心点为圆心
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;

        //根据父控件布局与子view的规格 得到包裹圆的矩形
        mRectF.set(centerX - mRadius, centerY - mRadius, centerX + mRadius, centerY + mRadius);
        tickRadiusOffset=DisplayUtils.dp2px(mContext, 28);
        //设置打钩的几个点坐标
        final float startX = centerX - mRadius + tickRadiusOffset;
        final float startY = (float) centerY;

        final float middleX = centerX - mRadius / 2 + tickRadiusOffset;
        final float middleY = centerY + mRadius / 2;

        final float endX = centerX + mRadius * 2 / 4 ;
        final float endY = centerY - mRadius * 2 / 4;
        //画勾子
        path.reset();
        path.moveTo(startX, startY);
        path.lineTo(middleX, middleY);
        path.lineTo(endX, endY);
    }



    @Override
    protected void onDraw(Canvas canvas) {//绘制
        super.onDraw(canvas);
        if(!isChecked){//没被点击
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            paint.setColor(uncheck_base_color);
            canvas.drawArc(mRectF, 90, 360, false, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            paint.setColor(uncheck_base_color);
            canvas.drawPath(path, paint);
            return;
        }else {
            drawProgress(canvas);
        }
    }

    private void drawProgress(final Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        paint.setColor(check_base_color);
        if (getProgressCount() >= 360) {
            //画一个黄色实心圈
            drawYellowCircle(canvas,paint);
            //画一个半径缩小的白色圆
            drawWhiteChangeCircle(canvas, paint);
        }else {
            canvas.drawArc(mRectF, 90, progressCount, false, paint);
        }
    }

    private void drawYellowCircle(Canvas canvas,Paint mPaintRing) {
        mPaintRing.setStyle(Paint.Style.FILL);
        mPaintRing.setColor(check_base_color);
        canvas.drawCircle(centerX,centerY,mRadius,mPaintRing);
    }


    private void drawWhiteChangeCircle(Canvas canvas, Paint mPaintRing) {
          mPaintRing.setColor(Color.WHITE);
        if(backRadius<=0){
            drawScale(canvas,mPaintRing);
            drawAlphaYes(canvas, mPaintRing);
        }
        canvas.drawCircle(centerX,centerY,backRadius,mPaintRing);
    }


    private void drawScale(Canvas canvas, Paint mPaintRing) {
        mPaintRing.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintRing.setColor(check_base_color);
        mPaintRing.setStrokeWidth(getRingStrokeWidth());
        canvas.drawCircle(centerX,centerY,mRadius,mPaintRing);
    }

    private void drawAlphaYes(Canvas canvas, Paint mPaintRing) {
        mPaintRing.setStyle(Paint.Style.STROKE);
        mPaintRing.setStrokeWidth(15);
        mPaintRing.setColor(check_tick_color);
        mPaintRing.setAlpha(alphaCount);
        canvas.drawPath(path, mPaintRing);
    }


    ////////////////////////////////////////////
    /**
     *  是否选中
     */
    public void setChecked(boolean checked) {
        if (this.isChecked != checked) {
            isChecked = checked;
            reset(isChecked);
        }
    }

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public interface OnCheckedChangeListener {
        void onCheckedChanged(ConfirmView tickView, boolean isCheck);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.mOnCheckedChangeListener = listener;
    }


    private void setUpEvent() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isChecked = !isChecked;
                reset(isChecked);
                if (mOnCheckedChangeListener != null) {
                    //此处回调
                    mOnCheckedChangeListener.onCheckedChanged((ConfirmView) view, isChecked);
                }
            }
        });
    }



    private void reset(boolean isChecked){
        if(isChecked){
            mFinalAnimatorSet.start();
        }else {
            mFinalAnimatorSet.cancel();
        }
        invalidate();
    }


    //=====================属性动画=================================

    public int getProgressCount() {
        return progressCount;
    }

    public void setProgressCount(int progressCount) {
        this.progressCount = progressCount;
        postInvalidate();
    }


    public int getBackRadius() {
        return backRadius;
    }

    public void setBackRadius(int backRadius) {
        this.backRadius = backRadius;
        postInvalidate();
    }

    public int getAlphaCount() {
        return alphaCount;
    }

    public void setAlphaCount(int alphaCount) {
        this.alphaCount = alphaCount;
        postInvalidate();
    }

    public float getRingStrokeWidth() {
        return ringStrokeWidth;
    }

    public void setRingStrokeWidth(float ringStrokeWidth) {
        this.ringStrokeWidth = ringStrokeWidth;
        postInvalidate();
    }

    private void initAnim() {

        ObjectAnimator mProgress = ObjectAnimator.ofInt(this, "progressCount", 0, 360);
        mProgress.setDuration(defaultRateEnum.getRingCounterUnit()*1000);//用执行时间改变速度
        mProgress.setInterpolator(null);



        ObjectAnimator mCircleAnimator = ObjectAnimator.ofInt(this, "backRadius", (int)mRadius, 0);
        mCircleAnimator.setInterpolator(new LinearInterpolator());
        mCircleAnimator.setDuration(defaultRateEnum.getCircleCounterUnit()*500);



        ObjectAnimator mAlphaAnimator = ObjectAnimator.ofInt(this, "alphaCount", 0,255);
        mAlphaAnimator.setInterpolator(null);
        mAlphaAnimator.setDuration(1000);

        ObjectAnimator mScaleAnimator = ObjectAnimator.ofFloat(this, "ringStrokeWidth", 1,50, 1);
        mScaleAnimator.setInterpolator(null);
        mScaleAnimator.setDuration(1000);

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(mAlphaAnimator,mScaleAnimator);

        mFinalAnimatorSet = new AnimatorSet();
        mFinalAnimatorSet.playSequentially(mProgress,mCircleAnimator,animatorSet);
    }
}
