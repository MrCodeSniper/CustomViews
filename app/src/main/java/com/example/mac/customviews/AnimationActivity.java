package com.example.mac.customviews;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.mac.customviews.View.GoogleMapAnimView.MapAnimView;

/**
 * Created by mac on 2018/4/21.
 */

public class AnimationActivity extends Activity {

    private MapAnimView mapAnimView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        mapAnimView=(MapAnimView)findViewById(R.id.mapview);
        mapAnimView.startAnim();
    }
}
