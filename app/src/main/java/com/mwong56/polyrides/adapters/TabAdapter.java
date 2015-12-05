package com.mwong56.polyrides.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mwong56.polyrides.fragments.BaseTabbedFragment;
import com.mwong56.polyrides.fragments.ChatFragment;
import com.mwong56.polyrides.fragments.DriverFragment;
import com.mwong56.polyrides.fragments.MyRidesFragment;
import com.mwong56.polyrides.fragments.PassengerFragment;

/**
 * Created by micha on 10/8/2015.
 */
public class TabAdapter extends FragmentStatePagerAdapter {

  private static final String[] items = {"Driver", "Passenger", "My Rides", "Chat"};
  private static final BaseTabbedFragment[] fragments = {DriverFragment.newInstance(), PassengerFragment.newInstance(),
      MyRidesFragment.newInstance(), ChatFragment.newInstance()};
  private Context context;

  public TabAdapter(FragmentManager fm, Context context) {
    super(fm);
    this.context = context;

    for (int i = 1; i < fragments.length; i++) {
      fragments[i].onHidden();
    }
  }

  public BaseTabbedFragment[] getFragments() {
    return this.fragments;
  }

  @Override
  public Fragment getItem(int position) {
    if (position > fragments.length) {
      return null;
    }
    return fragments[position];
  }

  @Override
  public int getCount() {
    return items.length;
  }

  @Override
  public String getPageTitle(int position) {
    return items[position];
  }
}
