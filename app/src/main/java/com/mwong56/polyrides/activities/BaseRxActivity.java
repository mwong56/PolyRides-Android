package com.mwong56.polyrides.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.utils.BusHolder;
import com.squareup.otto.Bus;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import icepick.Icepick;

/**
 * Created by micha on 10/22/2015.
 */
public abstract class BaseRxActivity extends RxAppCompatActivity {

  protected final Bus bus = BusHolder.get();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Icepick.restoreInstanceState(this, savedInstanceState);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Icepick.saveInstanceState(this, outState);
  }

  @Override
  protected void onResume() {
    super.onResume();
    bus.register(this);
  }

  @Override
  protected void onPause() {
    bus.unregister(this);
    super.onPause();
  }

  public void showToast(Throwable e) {
    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
  }

  public void showToast(String s) {
    Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
  }

  protected void replaceFragment(Fragment fragment, String tag) {
    getSupportFragmentManager()
        .beginTransaction()
        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
        .replace(R.id.frame_layout, fragment, tag)
        .addToBackStack(tag)
        .commit();
  }
}
