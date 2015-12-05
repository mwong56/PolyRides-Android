package com.mwong56.polyrides.utils;

import com.squareup.otto.Bus;

/**
 * Created by micha on 12/1/2015.
 */
public class BusHolder {
  private static final Bus bus = new Bus();

  private BusHolder() {
  }

  public static Bus get() {
    return bus;
  }

}
