package com.mwong56.polyrides.fragments;

import android.os.Bundle;
import android.widget.Toast;

import com.mwong56.polyrides.application.PolyRidesApp;
import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle.components.support.RxFragment;

import icepick.Icepick;

/**
 * Created by micha on 10/22/2015.
 */
public abstract class BaseRxFragment extends RxFragment {

//  protected Bus bus = BusSingleton.get();

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

  @Override
  public void onResume() {
    super.onResume();
//    bus.register(this);
  }

  @Override
  public void onPause() {
    super.onPause();
//    bus.unregister(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    RefWatcher refWatcher = PolyRidesApp.getRefWatcher(getActivity());
    refWatcher.watch(this);
  }

  protected void showToast(Throwable e) {
    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
  }
}
