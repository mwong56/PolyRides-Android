package com.mwong56.polyrides.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.mwong56.polyrides.R;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import icepick.Icepick;

/**
 * Created by micha on 10/22/2015.
 */
public abstract class BaseRxActivity extends RxAppCompatActivity {

//  protected Bus bus = BusSingleton.get();

  @Override
  public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
    super.onCreate(savedInstanceState, persistentState);
    Icepick.restoreInstanceState(this, savedInstanceState);
  }

  @Override
  public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    super.onSaveInstanceState(outState, outPersistentState);
    Icepick.saveInstanceState(this, outState);
  }

  @Override
  protected void onResume() {
    super.onResume();
//    bus.register(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
//    bus.unregister(this);
  }

  protected void showToast(Throwable e) {
    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
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
