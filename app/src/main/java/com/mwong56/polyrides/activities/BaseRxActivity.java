package com.mwong56.polyrides.activities;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.mwong56.polyrides.R;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by micha on 10/22/2015.
 */
public class BaseRxActivity extends RxAppCompatActivity {

  protected void showToast(Throwable e) {
    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
  }

  protected void replaceFragment(Fragment fragment, String tag) {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.frame_layout, fragment, tag)
        .addToBackStack(tag)
        .commit();
  }
}
