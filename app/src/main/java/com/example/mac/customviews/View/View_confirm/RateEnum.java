package com.example.mac.customviews.View.View_confirm;

/**
 * Created by mac on 2018/4/16.
 */

public enum RateEnum {

    //低速
    SLOW(6, 4, 2),
    //正常速度
    NORMAL(3, 2, 4),
    //高速
    FAST(2, 1, 8);

    public static final int RATE_MODE_SLOW = 0;
    public static final int RATE_MODE_NORMAL = 1;
    public static final int RATE_MODE_FAST = 2;


    //圆环进度增加的单位t
    private int ringCounterUnit;
    //圆圈收缩的单位
    private int circleCounterUnit;
    //圆圈最后放大收缩的单位
    private int scaleCounterUnit;



    RateEnum(int i, int i1, int i2) {

        this.ringCounterUnit=i;
        this.circleCounterUnit=i1;
        this.scaleCounterUnit=i2;

    }


    public static RateEnum getRateEnum(int rateMode) {
        RateEnum tickRateEnum;
        switch (rateMode) {
            case RATE_MODE_SLOW:
                tickRateEnum = RateEnum.SLOW;
                break;
            case RATE_MODE_NORMAL:
                tickRateEnum = RateEnum.NORMAL;
                break;
            case RATE_MODE_FAST:
                tickRateEnum = RateEnum.FAST;
                break;
            default:
                tickRateEnum = RateEnum.NORMAL;
                break;
        }
        return tickRateEnum;
    }


    public int getRingCounterUnit() {
        return ringCounterUnit;
    }

    public void setRingCounterUnit(int ringCounterUnit) {
        this.ringCounterUnit = ringCounterUnit;
    }

    public int getCircleCounterUnit() {
        return circleCounterUnit;
    }

    public void setCircleCounterUnit(int circleCounterUnit) {
        this.circleCounterUnit = circleCounterUnit;
    }

    public int getScaleCounterUnit() {
        return scaleCounterUnit;
    }

    public void setScaleCounterUnit(int scaleCounterUnit) {
        this.scaleCounterUnit = scaleCounterUnit;
    }
}
