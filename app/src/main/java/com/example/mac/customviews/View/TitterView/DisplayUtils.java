package com.example.mac.customviews.View.TitterView;

import android.content.Context;

/**
 * Created by mac on 2018/4/16.
 */

public class DisplayUtils {
    static int dp2px(Context context, float dpValue) {
        if (context == null) return (int) (dpValue * 1.5f + 0.5f);
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
