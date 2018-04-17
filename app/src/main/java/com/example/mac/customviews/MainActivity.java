package com.example.mac.customviews;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mac.customviews.View.IndexBar.MyIndexBar;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private MyIndexBar indexbar;
    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.currentindex);
        indexbar=findViewById(R.id.indexbar);

        indexbar.setonTouchListener(new MyIndexBar.onTouchLetterListener() {
            @Override
            public void onTouchLetter(String letter) {
                textView.setText(letter);
                textView.setVisibility(View.VISIBLE);
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView.setVisibility(View.INVISIBLE);
                    }
                }, 1000);
            }
        });





    }
}
