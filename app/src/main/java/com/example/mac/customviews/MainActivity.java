package com.example.mac.customviews;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.customviews.View.IndexBar.MyIndexBar;
import com.example.mac.customviews.View.ParallexList.ParallexView;
import com.example.mac.customviews.View.SwipeLayout.SwipeLayout;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private TextView textView;
    private MyIndexBar indexbar;
    private Handler handler=new Handler();

    private ParallexView listView;
    private ArrayList<String> list=new ArrayList<String>();

    private Button btn_modle;
    private Button btn_changemodle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.currentindex);
        indexbar=findViewById(R.id.indexbar);
        btn_changemodle=findViewById(R.id.btn_changemodle);
        btn_modle=findViewById(R.id.btn_modle);


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


        listView = (ParallexView) findViewById(R.id.listview);
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);//永远不显示头顶的蓝色阴影
        View headerview= View.inflate(this,R.layout.layout_header,null);
        ImageView imageView= (ImageView) headerview.findViewById(R.id.imageView);
        listView.addHeaderView(headerview);
        for(int i=0;i<30;i++){
            list.add("第"+i+"条数据");
        }

        final Myadapter myadapter=new Myadapter();
        listView.setAdapter(myadapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("xxx","onItemClick");
                Toast.makeText(MainActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
            }
        });




        listView.setParallexImageView(imageView);
        btn_changemodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myadapter.isEdit()){
                    myadapter.setEdit(false);
                    btn_modle.setText("Mode:普通模式");
                }else {
                    myadapter.setEdit(true);
                    btn_modle.setText("Mode:编辑模式");
                }
                myadapter.notifyDataSetChanged();
            }
        });







    }




    class Myadapter extends BaseAdapter implements SwipeLayout.OnSwipeStateChangedListener{

        private boolean isEdit=false;

        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=View.inflate(MainActivity.this,R.layout.adapter_list,null);
            }
            ViewHolder viewHolder=ViewHolder.getHolder(convertView);
            viewHolder.tv_name.setText(list.get(position));
            viewHolder.swipeLayout.setTag(position);
            viewHolder.swipeLayout.setOnSwipeStateChangedListener(this);
            viewHolder.swipeLayout.setEdit(isEdit);
            return convertView;
        }
        @Override
        public void onOpen(Object tag) {
            Toast.makeText(MainActivity.this,"第"+(Integer)tag+"个打开",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onClose(Object tag) {
        }

        public void setEdit(boolean edit) {
            isEdit = edit;
        }

        public boolean isEdit() {
            return isEdit;
        }
    }

    static class ViewHolder{
        TextView tv_name,tv_delete;
        SwipeLayout swipeLayout;
        ImageView iv;
        public ViewHolder(View convertview){
            tv_name= (TextView) convertview.findViewById(R.id.tv_name);
            tv_delete= (TextView) convertview.findViewById(R.id.tv_delete);
            iv= (ImageView) convertview.findViewById(R.id.iv);
            swipeLayout= (SwipeLayout) convertview.findViewById(R.id.swipelayout);
        }
        public  static ViewHolder getHolder(View convertview){
            ViewHolder viewHolder= (ViewHolder) convertview.getTag();
            if(viewHolder==null){
                viewHolder=new ViewHolder(convertview);
                convertview.setTag(viewHolder);
            }
            return  viewHolder;
        }
    }
}
