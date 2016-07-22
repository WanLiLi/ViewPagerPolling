package com.wanli.com.viewpagerpolling;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by wanli on 2016/7/12.
 */
public class MyViewPagerAdapter extends PagerAdapter {
    private List<ImageView> imageList;
    private Context mContext;

    public MyViewPagerAdapter(List<ImageView> imageList, Context context) {
        this.mContext = context;
        this.imageList = imageList;
    }

    //这个方法用来实例化页卡
    //当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        (container).addView(imageList.get(position));
        return imageList.get(position);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }


    // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
    // 方法用于判断某个view是否与key对象关联。
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;   //官方提示这样写
    }

    // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView(imageList.get(position));
    }


}
