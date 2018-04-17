package com.example.mac.customviews.View.TitterView;

/**
 * Created by mac on 2018/4/16.
 */

public class Config {


    private RateEnum defaultRateEnum;

    private float mRadius;

    private int uncheck_base_color;
    private int check_base_color;
    private int check_tick_color;


    public RateEnum getDefaultRateEnum() {
        return defaultRateEnum;
    }

    public void setDefaultRateEnum(RateEnum defaultRateEnum) {
        this.defaultRateEnum = defaultRateEnum;
    }

    public float getmRadius() {
        return mRadius;
    }

    public void setmRadius(float mRadius) {
        this.mRadius = mRadius;
    }

    public int getUncheck_base_color() {
        return uncheck_base_color;
    }

    public void setUncheck_base_color(int uncheck_base_color) {
        this.uncheck_base_color = uncheck_base_color;
    }

    public int getCheck_base_color() {
        return check_base_color;
    }

    public void setCheck_base_color(int check_base_color) {
        this.check_base_color = check_base_color;
    }

    public int getCheck_tick_color() {
        return check_tick_color;
    }

    public void setCheck_tick_color(int check_tick_color) {
        this.check_tick_color = check_tick_color;
    }
}
