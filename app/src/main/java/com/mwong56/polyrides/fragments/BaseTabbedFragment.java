package com.mwong56.polyrides.fragments;

import android.os.Bundle;

/**
 * Created by micha on 12/5/2015.
 */
public abstract class BaseTabbedFragment extends BaseRxFragment {

  private boolean registered = false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRegisterEvents(false);
  }

  public void onVisible() {
    registerBus();
  }


  public void onHidden() {
    unregisterBus();
  }

  void registerBus() {
    if (!registered) {
      registered = true;
      bus.register(this);
    }
  }

  void unregisterBus() {
    if (registered) {
      registered = false;
      bus.unregister(this);
    }
  }
}
