package com.example.b2capp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fl_main;
    private RadioButton rb_main;
    private RadioButton rb_category;
    private RadioButton rb_search;
    private RadioButton rb_car;
    private RadioButton rb_me;
    private RadioGroup rg_tab;
    private int index;//记录点击的tab位置
    private int currentIndex;//当前显示的tab位置
    private Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        fragments = new Fragment[5];
        fragments[0] = new HomeFragment();
        fragments[1] = new CategoryFragment();
        fragments[2] = new SearchFragment();
        fragments[3] = new CarFragment();
        fragments[4] = new MeFragment();

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.fl_main, fragments[0]).show(fragments[0]).commit();
    }

    private void initView() {
        fl_main = (FrameLayout) findViewById(R.id.fl_main);
        rb_main = (RadioButton) findViewById(R.id.rb_home);
        rb_category = (RadioButton) findViewById(R.id.rb_category);
        rb_search = (RadioButton) findViewById(R.id.rb_search);
        rb_car = (RadioButton) findViewById(R.id.rb_car);
        rb_me = (RadioButton) findViewById(R.id.rb_me);
        rg_tab = (RadioGroup) findViewById(R.id.rg_tab);

        rg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        index = 0;
                        break;
                    case R.id.rb_category:
                        index = 1;
                        break;
                    case R.id.rb_search:
                        index = 2;
                        break;
                    case R.id.rb_car:
                        index = 3;
                        break;
                    case R.id.rb_me:
                        index = 4;
                        break;
                }

                showFragment(index);
            }
        });
    }


    /**
     * 切换fragment
     *
     * @param index：0-4
     */
    public void showFragment(int index) {
        if (currentIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fl_main, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        currentIndex = index;
    }
}
