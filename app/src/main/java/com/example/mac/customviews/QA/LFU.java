package com.example.mac.customviews.QA;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *  LFU (Least Frequently Used)
 *  LFU 是最近最不常使用算法，通过为每个元素维护一个计数器，插入的时候计数器默认为1。
    如果容量满了，则删除计数器最小的元素，再插入新的。访问数据的时候，如果存在返回value并计数器+1否则放回nil。以下是java代码实现
 *
 *
 * Created by mac on 2018/4/16.
 */

public class LFU<T> {

    HashMap<T,Integer> elements;
    private int maxsize;

    public LFU(int maxsize) {
        this.maxsize = maxsize;
        elements=new HashMap<>(maxsize);
    }

    public int getCount(){
        return elements.size();
    }

    public Integer visit(T t){
        if(elements.get(t)!=null){
            Integer counter=elements.get(t);
            counter+=1;
            elements.put(t,counter);
            return counter;
        }
        return null;
    }


    public int set(T t){
        if(elements.get(t)!=null){
            Integer counter=elements.get(t);
            counter+=1;
            elements.put(t,counter);
            return counter;
        }else {
            if(getCount()>=maxsize){
                elements.remove(pop());
            }
            elements.put(t,1);
            return 1;
        }
    }


    public T pop(){
        T min=null;
        Integer minValue=Integer.MAX_VALUE;
        if(getCount()>=maxsize){
            for (T t:elements.keySet()) {

                if(elements.get(t)<minValue){
                    min=t;
                    minValue=elements.get(t);
                }
            }
        }
        return min;
    }

}
