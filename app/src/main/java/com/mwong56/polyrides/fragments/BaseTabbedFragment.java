package com.mwong56.polyrides.fragments;

import android.os.Bundle;

/**
 * Created by micha on 12/5/2015.
 */
public abstract class BaseTabbedFragment extends BaseRxFragment {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRegisterEvents(false);
  }

  public void onHidden() {
  }


  public void onVisible() {
  }

}
