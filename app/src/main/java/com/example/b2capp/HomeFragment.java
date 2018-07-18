package com.example.b2capp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 首页
 *
 * @Date 2018-07-11.
 */
public class HomeFragment extends Fragment {
    private ArrayList<Integer> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        list.add(R.mipmap.ic_pic0);
        list.add(R.mipmap.ic_pic2);
        list.add(R.mipmap.ic_pic3);
        list.add(R.mipmap.ic_pic4);

        ShufflingViewPager viewPager = (ShufflingViewPager) view.findViewById(R.id.viewPager);
        viewPager.setPointRes(R.drawable.point_unchecked, R.drawable.point_checked, 20)
                .setSwitchable(true)
                .setTime(2000)
                .build(list);
        return view;
    }
}
