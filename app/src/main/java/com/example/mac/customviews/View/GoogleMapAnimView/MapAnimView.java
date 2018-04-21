package com.example.mac.customviews.View.GoogleMapAnimView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.example.mac.customviews.R;

/**
 *开始动画，一个Y轴旋转45度动效，看过hencoder教程的童鞋应该很轻松实现
 *中间动画，比较复杂，后面着重分解
 *结束动画，一个绕X轴旋转45度动效，看过hencoder教程的童鞋应该很轻松实现
 * Created by mac on 2018/4/21.
 */

public class MapAnimView extends View {

    private Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap bitmap;
    private RectF rectF;
    private AnimatorSet animatorSet = new AnimatorSet();
    private Camera camera = new Camera();
    //裁切部分Path
    private Path path=new Path();
    //Y轴方向旋转角度
    private float degreeY;
    //不变的那一半，Y轴方向旋转角度
    private float fixDegreeY;
    //Z轴方向（平面内）旋转的角度
    private float degreeZ;

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flip_boards);



        /**
         * 整个动画被拆分成为三个部分
         * 1、绕Y轴3D旋转45度
         * 2、绕Z轴3D旋转270度
         * 3、不变的那一半（上半部分）绕Y轴旋转30度（注意，这里canvas已经旋转了270度，计算第三个动效参数时要注意）
         */
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(this, "degreeY", 0, -45);
        animator1.setDuration(2000);
        animator1.setStartDelay(500);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(this, "degreeZ", 0, 270);
        animator2.setDuration(1500);
        animator2.setStartDelay(500);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(this, "fixDegreeY", 0, 30);
        animator3.setDuration(1000);
        animator3.setStartDelay(500);

        animatorSet.playSequentially(animator1,animator2,animator3);



        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float newZ = -displayMetrics.density * 6;
        camera.setLocation(0, 0, newZ);
    }



    public MapAnimView(Context context) {
        super(context);
    }

    public MapAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MapAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animatorSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animatorSet.end();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int x = centerX - bitmapWidth / 2;
        int y = centerY - bitmapHeight / 2;

        //画变换的一半
        //先旋转，再裁切，再使用camera执行3D动效,**然后保存camera效果**,最后再旋转回来
        canvas.save();
        camera.save();
        canvas.translate(centerX, centerY);

        canvas.rotate(-degreeZ);//坐标系旋转 图形投影本来不会变的
        camera.rotateY(degreeY);//随着时间慢慢转y轴 左边近右边远 左边凸右边凹
        camera.applyToCanvas(canvas);//获得投影
        //计算裁切参数时清注意，此时的canvas的坐标系已经移动
        canvas.clipRect(0, -centerY, centerX, centerY);//留右边的一半矩形
        canvas.rotate(degreeZ);//坐标系转回来

        canvas.translate(-centerX, -centerY);
        camera.restore();
        canvas.drawBitmap(bitmap, x, y, bitmapPaint);
        canvas.restore();

        //画不变换的另一半
        canvas.save();
        camera.save();
        canvas.translate(centerX, centerY);


        canvas.rotate(-degreeZ);//这么做
        //计算裁切参数时清注意，此时的canvas的坐标系已经移动
        canvas.clipRect(-centerX, -centerY, 0, centerY);
        //此时的canvas的坐标系已经旋转，所以这里是rotateY
        camera.rotateY(fixDegreeY);
        camera.applyToCanvas(canvas);
        canvas.rotate(degreeZ);//这么做只是为了造成视觉差异

        canvas.translate(-centerX, -centerY);
        camera.restore();
        canvas.drawBitmap(bitmap, x, y,bitmapPaint);
        canvas.restore();
    }



    public void startAnim(){
        animatorSet.end();
        degreeY = 0;
        fixDegreeY = 0;
        degreeZ = 0;
        invalidate();
        animatorSet.start();
    }



    //////////////////////////////////////////////////////



    public void setDegreeY(float degreeY) {
        this.degreeY = degreeY;
        invalidate();
    }

    public void setFixDegreeY(float fixDegreeY) {
        this.fixDegreeY = fixDegreeY;
        invalidate();
    }

    public void setDegreeZ(float degreeZ) {
        this.degreeZ = degreeZ;
        invalidate();
    }
}
