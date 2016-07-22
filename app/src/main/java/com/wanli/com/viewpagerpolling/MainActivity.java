package com.wanli.com.viewpagerpolling;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewpager1;
    private LinearLayout linear;
    private Button btn_go;

    private List<ImageView> imageList;
    private int[] images;

    private List<ImageView> circleIamgeList;


    private Timer timer;
    //当前轮播页
    private int currentItem = 0;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewpager1.setCurrentItem(currentItem);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        viewpager1 = (ViewPager) findViewById(R.id.viewpager1);
        linear = (LinearLayout) findViewById(R.id.linear);
        btn_go = (Button) findViewById(R.id.btn_go);


        /**
         * 设置滚动的图片的资源
         *
         * */
        images = new int[]{R.mipmap.a1, R.mipmap.a2, R.mipmap.a3, R.mipmap.a4};
        imageList = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundResource(images[i]);
            imageList.add(imageView);
        }


        /**
         * 设置圆点图片的资源
         *
         * */
        circleIamgeList = new ArrayList<>();
        linear.removeAllViews();
        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundResource(R.drawable.circle);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 15;
            params.rightMargin = 15;
            //加上第二个参数
            linear.addView(imageView, params);
            circleIamgeList.add(imageView);
        }
        linear.getChildAt(currentItem).setBackgroundResource(R.drawable.circle_solid);
    }


    private void initEvent() {
        viewpager1.setAdapter(new MyViewPagerAdapter(imageList, this));
        viewpager1.addOnPageChangeListener(new OnPagerListener());


        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                currentItem = (currentItem + 1) % imageList.size();
                Log.d("wanli", "timerTask:" + currentItem);
                handler.obtainMessage().sendToTarget();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 5000, 5000);

        //初始为0，不会触发onPageSelected
        //viewpager1.setCurrentItem(0);
    }


    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     */
    class OnPagerListener implements ViewPager.OnPageChangeListener {

        //1.从0到1，从1到2，从2到3
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.d("wanli", "onPageScrolled:" + position);
        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;

            setAnimatorByCircle(position);

            for (int i = 0; i < linear.getChildCount(); i++) {
                if (i == position) {
                    linear.getChildAt(i).setBackgroundResource(R.drawable.circle_solid);
                } else {
                    linear.getChildAt(i).setBackgroundResource(R.drawable.circle);
                }

                if (imageList.size() - 1 == position) {
                    btn_go.setVisibility(View.VISIBLE);
                }else {
                    btn_go.setVisibility(View.GONE);
                }
            }
        }


        /**
         * arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做
         * 当页面开始滑动的时候，三种状态的变化顺序为（1，2，0）
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                // 滑动结束，即切换完毕或者加载完毕
                case 0:
                    break;
                // 滑动完毕了,界面切换中
                case 2:
                    break;
                // 正在滑动
                case 1:
                    break;
            }
        }
    }

    private void setAnimatorByCircle(int position) {
        ObjectAnimator caleY = ObjectAnimator.ofFloat(circleIamgeList.get(position), "scaleY", 0, 1f);
        ObjectAnimator caleX = ObjectAnimator.ofFloat(circleIamgeList.get(position), "scaleX", 0, 1f);
        //ObjectAnimator translationX = ObjectAnimator.ofFloat(circleIamgeList.get(position),"translationX", 0, 30f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(600);
        set.setInterpolator(new LinearInterpolator());
        set.play(caleY).with(caleX);
        set.start();
    }
}
