package com.mwong56.polyrides.fragments;

import android.os.Bundle;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxFragment;

import icepick.Icepick;

/**
 * Created by micha on 10/22/2015.
 */
public abstract class BaseRxFragment extends RxFragment {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Icepick.restoreInstanceState(this, savedInstanceState);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Icepick.saveInstanceState(this, outState);

  }

  protected void showToast(Throwable e) {
    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
  }
}
