package com.example.mac.customviews.View.SwipeLayout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 无需onMeasure FrameLayout帮我们弄好了
 * Created by mac on 2018/4/18.
 */

public class SwipeLayout extends FrameLayout{
    private ViewDragHelper viewDragHelper;//拖拽帮助类 能轻松实现拖拽 当然不用也可以根据触摸实现拖拽
    private View content_view;
    private View delete_view;
    private int content_height;
    private int delete_width;
    private int content_width;
    private int delete_height;

    public SwipeLayout(Context context) {
        super(context);
        viewDragHelper=ViewDragHelper.create(this,MyCallBack);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewDragHelper=ViewDragHelper.create(this,MyCallBack);
    }

    //onFinishInflate方法是在setContentView之后、onMeasure之前 将xml转为view的时候回调
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        content_view = getChildAt(0);//内容view
        delete_view = getChildAt(1);//拖拽view
    }

    //onMeasure完毕后用来拿测量数据
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        content_width = content_view.getMeasuredWidth();
        content_height = content_view.getMeasuredHeight();
        delete_width = delete_view.getMeasuredWidth();
        delete_height = delete_view.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //摆放位置
        content_view.layout(0,0,content_width,content_height);
        delete_view.layout(content_view.getRight(),0,content_view.getRight()+delete_width,content_height);
    }



    public void close() {
        viewDragHelper.smoothSlideViewTo(content_view,0,content_view.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);//刷新动画
    }

    public void open() {
        viewDragHelper.smoothSlideViewTo(content_view,-delete_width,content_view.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);//刷新动画
    }


    //========================================================================================================================



    private ViewDragHelper.Callback MyCallBack=new ViewDragHelper.Callback() {
        //// 决定了是否需要捕获这个 child，只有捕获了才能进行下面的拖拽行为
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == content_view || child == delete_view;
        }

        // 修整 child 水平方向上的坐标，left 指 child 要移动到的坐标，dx 相对上次的偏移量
        //往右 left>0 往左left<0
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //left表示即将移动到的位置
            if (child == content_view) {//拉的是内容view
                if (left > 0) {//右拉不了
                    left = 0;
                } else if (left < -delete_width) {//左拉最多为deleteview宽
                    left = -delete_width;
                }
            } else if (child == delete_view) {//拉的是deleteview
                //？？？
                if (left < content_width - delete_width) left = content_width - delete_width;
                if (left > content_width) left = content_width;//不超出边界
            }
            return left;
        }

        // 手指释放时的回调
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (content_view.getLeft() < -delete_width / 2) {
               // Log.e("xxx","onViewReleased-open!!!");
                  open();
            } else {
              //  Log.e("xxx","onViewReleased-close!!!");
                  close();
            }
        }


        //在边界拖动时回调 最多拖动边界宽度长
        @Override
        public int getViewHorizontalDragRange(View child) {
            return delete_width;
        }


        //当captureView的位置发生变化是回调
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == content_view) {
                delete_view.layout(delete_view.getLeft() + dx, delete_view.getTop() + dy,
                        delete_view.getRight() + dx, delete_view.getBottom() + dy);
            } else if (changedView == delete_view) {
                content_view.layout(content_view.getLeft() + dx, content_view.getTop() + dy,
                        content_view.getRight() + dx, content_view.getBottom() + dy);
            }

            if (content_view.getLeft() == 0 && currentState != SwipeState.Close) {//关闭状态
                currentState=SwipeState.Close;
                    if(swipeStateChangedListener!=null){
                        swipeStateChangedListener.onClose(getTag());
                    }
                SwipeLayoutManager.getInstance().clearCurrentLayout();
            } else if (content_view.getLeft() == -delete_width && currentState != SwipeState.Open) {//开启状态
                currentState=SwipeState.Open;
                if(swipeStateChangedListener!=null){
                    swipeStateChangedListener.onOpen(getTag());
                }
                SwipeLayoutManager.getInstance().setSwipeLayout(SwipeLayout.this);
            }
        }
    };
    //记录状态
    enum SwipeState{
        Open,Close;
    }
    private SwipeState currentState=SwipeState.Close;


    ////监听器
    public void setOnSwipeStateChangedListener(OnSwipeStateChangedListener swipeStateChangedListener) {
        this.swipeStateChangedListener = swipeStateChangedListener;
    }
    private OnSwipeStateChangedListener swipeStateChangedListener;
    public interface OnSwipeStateChangedListener{
        void onOpen(Object tag);
        void onClose(Object tag);
        //还可能拖拽百分比
    }



    //////////////触摸事件


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result=viewDragHelper.shouldInterceptTouchEvent(ev);//判断是否viewDragHelper拦截
        //如果当前有打开的，则需要直接拦截，交给onTouch处理
        if(!SwipeLayoutManager.getInstance().isShouldSwipe(this)){//点击的view不是当前设置的swipe就关闭
            //先关闭打开的layout
            SwipeLayoutManager.getInstance().closeCurrentLayout();//不要放在ontouch里因为会一直调用卡顿
            Log.e("xxx","onInterceptTouchEvent消费了");
            return true;//交给onTouch
        }
        return result;
    }

    private float downX;
    private float downY;
    private boolean isEdit=false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!SwipeLayoutManager.getInstance().isShouldSwipe(this)){
            requestDisallowInterceptTouchEvent(true);//请求父类不拦截，父类就不能滑动了
            Log.e("xxx","onTouchEvent消费了");
            return true;//不执行下面代码
        }


// 在拉动的过程中listview不滑动
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE://会一直执行这个函数
                float movex=event.getX();
                float movey=event.getY();
                float delatx=movex- downX;//移动的距离
                float delaty=movey- downY;
                //横向移动>竖直移动 认为是水平拖动
                if(Math.abs(delatx)>Math.abs(delaty)){//移动偏向水平方向,应该是swipelayout处理，listview不拦截
                    requestDisallowInterceptTouchEvent(true);
                    //告诉父组件，不要拦截我的事件
                    //子组件是可以正常响应事件的
                }
                downX=movex;
                downY=movey;
                //更新最新的落下坐标
                break;
        }

        viewDragHelper.processTouchEvent(event);////将触摸事件传给viewDragHelper来解析处理

        return isEdit;///消费掉此事件，自己来处理

    }


    @Override
    public void computeScroll() {
        if(viewDragHelper.continueSettling(true)){
            //是否应该继续移动
            ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
        }
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
