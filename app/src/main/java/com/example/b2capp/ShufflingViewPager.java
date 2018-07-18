package com.example.b2capp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * 自定义轮播图
 */
public class ShufflingViewPager extends RelativeLayout {

    private ViewPager viewPager;
    private LinearLayout llPoint;
    private ArrayList<Integer> list;
    private Handler mHandler;
    private int time = 3000;//切换间隔时间
    private int unSelectedResId, selectResId;//选中和未选中的图片资源
    private int widthPoint;//圆点大小
    private AdapterView.OnItemClickListener onItemClickListener;

    public ShufflingViewPager(Context context) {
        this(context, null);
    }

    public ShufflingViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShufflingViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //1.添加viewpager
        viewPager = new ViewPager(getContext());
        RelativeLayout.LayoutParams pagerParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        viewPager.setLayoutParams(pagerParams);
        addView(viewPager);

        //2.添加圆点
        llPoint = new LinearLayout(getContext());
        RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams
                (LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        llParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        llParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        llParams.bottomMargin = 15;
        llPoint.setLayoutParams(llParams);

        addView(llPoint);
    }

    /**
     * 设置小圆点的样式
     *
     * @param unSelectedResId 未选中的drawable
     * @param selectResId     选中的drawable
     * @param widthPoint      小圆点的大小
     * @return
     */
    public ShufflingViewPager setPointRes(int unSelectedResId, int selectResId, int widthPoint) {
        this.unSelectedResId = unSelectedResId;
        this.selectResId = selectResId;
        this.widthPoint = widthPoint;
        return this;
    }

    /**
     * 切换间隔时间
     *
     * @param delayMillis
     * @return
     */
    public ShufflingViewPager setTime(int delayMillis) {
        this.time = delayMillis;
        return this;
    }

    /**
     * 设置是否自动轮播
     *
     * @param isSwitchable
     * @return
     */
    @SuppressLint("HandlerLeak")
    public ShufflingViewPager setSwitchable(boolean isSwitchable) {
        if (!isSwitchable) {
            return this;
        }
        if (mHandler == null) {
            mHandler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                    mHandler.sendEmptyMessageDelayed(0, time);
                }
            };
        }
        mHandler.sendEmptyMessageDelayed(0, time);
        return this;
    }

    /**
     * 设置点击事件
     *
     * @param onItemClickListener
     * @return
     */
    public ShufflingViewPager setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    /**
     * 一些初始化，添加布局和设置数据
     *
     * @param list
     * @return
     */
    public ShufflingViewPager build(ArrayList<Integer> list) {
        this.list = list;
        viewPager.setAdapter(new MyPagerAdapter(getContext(), list));
        viewPager.setOnPageChangeListener(new GuideOnPageChangeListener());
        //初始化小圆点
        View view = null;
        LinearLayout.LayoutParams layoutParams = null;

        for (int i = 0; i < list.size(); i++) {
            view = new View(getContext());
            layoutParams = new LinearLayout.LayoutParams(widthPoint, widthPoint);
            layoutParams.rightMargin = 15;
            view.setLayoutParams(layoutParams);
            if (unSelectedResId != 0) {
                view.setBackgroundResource(unSelectedResId);
            } else {
                view.setBackgroundColor(Color.WHITE);
            }
            if (i == 0) {
                if (selectResId != 0) {
                    view.setBackgroundResource(selectResId);
                } else {
                    view.setBackgroundColor(Color.RED);
                }
            }
            llPoint.addView(view);
        }
        return this;
    }


    public class MyPagerAdapter extends PagerAdapter {
        private Context context;
        private ArrayList<Integer> list;

        public MyPagerAdapter(Context context, ArrayList<Integer> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            if (list.size() > 0) {
                ImageView iv = new ImageView(context);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setImageResource(list.get(position % list.size()));
                container.addView(iv);
                iv.setOnTouchListener(new TopNewsTouchListener());
                iv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        int pos = position % list.size();
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(null, null, pos, 0);
                        }
                    }
                });
                return iv;
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    public class TopNewsTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mHandler == null) {
                return false;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mHandler.removeCallbacksAndMessages(null);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    mHandler.sendEmptyMessageDelayed(0, 3000);
                    break;
                case MotionEvent.ACTION_UP:
                    mHandler.sendEmptyMessageDelayed(0, 3000);
                    break;
            }
            return false;
        }

    }

    /**
     * ViewPager的滑动监听
     *
     * @author DaQiang
     */
    @SuppressLint("NewApi")
    class GuideOnPageChangeListener implements ViewPager.OnPageChangeListener {

        // 滑动事件
        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        // 某个页面被选中
        @Override
        public void onPageSelected(int position) {
            // 选中页面对应的小圆点变色
            for (int i = 0; i < list.size(); i++) {
                if (position % list.size() == i) {
                    if (unSelectedResId != 0 && selectResId != 0) {
                        llPoint.getChildAt(i).setBackgroundResource(selectResId);
                    } else {
                        llPoint.getChildAt(i).setBackgroundColor(Color.RED);
                    }

                } else {
                    if (unSelectedResId != 0 && selectResId != 0) {
                        llPoint.getChildAt(i).setBackgroundResource(unSelectedResId);
                    } else {
                        llPoint.getChildAt(i).setBackgroundColor(Color.WHITE);
                    }

                }
            }
        }

        // 滑动状态发生变化
        @Override
        public void onPageScrollStateChanged(int state) {
            // TODO Auto-generated method stub
        }

    }

}