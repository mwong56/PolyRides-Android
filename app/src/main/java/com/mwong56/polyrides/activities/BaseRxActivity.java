package com.mwong56.polyrides.activities;

import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by micha on 10/22/2015.
 */
public class BaseRxActivity extends RxAppCompatActivity {

  protected void showToast(Throwable e) {
    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
  }
}
