package com.mwong56.polyrides.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mwong56.polyrides.fragments.DriverFragment;
import com.mwong56.polyrides.fragments.MessagesFragment;
import com.mwong56.polyrides.fragments.MyRidesFragment;
import com.mwong56.polyrides.fragments.PassengerFragment;

/**
 * Created by micha on 10/8/2015.
 */
public class TabAdapter extends FragmentPagerAdapter {

  private static final String[] items = {"Driver", "Passenger", "My Rides", "Messages"};

  private Context context;

  public TabAdapter(FragmentManager fm, Context context) {
    super(fm);
    this.context = context;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return DriverFragment.newInstance();
      case 1:
        return PassengerFragment.newInstance();
      case 2:
        return MyRidesFragment.newInstance();
      case 3:
        return MessagesFragment.newInstance();
    }
    return null;
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
