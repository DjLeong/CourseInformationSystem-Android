package com.dehua.courseinformationsystem.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dehua.courseinformationsystem.fragment.AnnouncementFragment;
import com.dehua.courseinformationsystem.fragment.AttendanceFragment;
import com.dehua.courseinformationsystem.fragment.HomePageFragment;
import com.dehua.courseinformationsystem.fragment.ScheduleFragment;

import java.util.ArrayList;

/**
 * Created by dehua on 16/2/22 022.
 */
public class FragmentController {

    public int containerId;
    private FragmentManager fragmentManager;
    private ArrayList<Fragment> fragments;
    private static FragmentController controller;

    public static void clearConrtoller(){
        controller=null;
    }

    public static FragmentController getInstance(FragmentActivity activity,int containerId) {
        if(controller==null) {
            controller = new FragmentController(activity, containerId);
        }
        return controller;
    }

    private FragmentController(FragmentActivity activity,int containerId) {
        this.containerId=containerId;
        fragmentManager=activity.getSupportFragmentManager();
        initFragment();
    }

    public void initFragment() {
        fragments=new ArrayList<Fragment>();
        fragments.add(new AnnouncementFragment());
        fragments.add(new AttendanceFragment());
        fragments.add(new ScheduleFragment());
        fragments.add(new HomePageFragment());
        FragmentTransaction ft=fragmentManager.beginTransaction();
        for(Fragment fragment:fragments){
            ft.add(containerId,fragment);
        }
        ft.commitAllowingStateLoss();
    }

    public void reloadFragment(){
        FragmentTransaction ft=fragmentManager.beginTransaction();
        for(Fragment fragment:fragments){
            if(fragment!=null){
                ft.remove(fragment);
            }
        }
        fragments=new ArrayList<Fragment>();
        fragments.add(new AnnouncementFragment());
        fragments.add(new AttendanceFragment());
        fragments.add(new ScheduleFragment());
        fragments.add(new HomePageFragment());
        for(Fragment fragment:fragments){
            ft.add(containerId,fragment);
        }
        ft.commitAllowingStateLoss();
    }

    public void showFragment(int position){
        hideFragments();
        Fragment fragment =fragments.get(position);
        FragmentTransaction ft=fragmentManager.beginTransaction();
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    public void hideFragments(){
        FragmentTransaction ft=fragmentManager.beginTransaction();
        for(Fragment fragment:fragments){
            if(fragment!=null){
                ft.hide(fragment);
            }
        }
        ft.commitAllowingStateLoss();
    }

    public Fragment getFragment(int position){
        return fragments.get(position);
    }

}
