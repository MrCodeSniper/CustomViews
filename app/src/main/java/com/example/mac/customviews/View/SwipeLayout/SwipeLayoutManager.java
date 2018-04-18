package com.example.mac.customviews.View.SwipeLayout;

/**
 * Created by mac on 2018/4/18.
 */

public class SwipeLayoutManager {
    private SwipeLayoutManager(){};//私有化构造函数
    private static SwipeLayoutManager mInstance=new SwipeLayoutManager();//懒汉
    public static SwipeLayoutManager getInstance(){//得到都是唯一的实例
        return mInstance;
    }
    private SwipeLayout currentLayout;//用来记录当前打开的SwipeLayout


    public boolean isShouldSwipe(SwipeLayout swipeLayout){
        if(currentLayout==null){//如果打开的布局没有那么就可以滑
            return  true;
        }else {
            return currentLayout==swipeLayout;//如果不是当前打开的布局，就不能滑动，如果就是这个打开的布局则可以滑动
        }
    }

    //关闭当前打开的layout
    public void  closeCurrentLayout(){
        if(currentLayout!=null){
            currentLayout.close();
        }
    }

    public  void setSwipeLayout(SwipeLayout layout){//将打开的layout设置进来
        this.currentLayout=layout;
    }


    public void clearCurrentLayout(){
        currentLayout=null;
    }





}
