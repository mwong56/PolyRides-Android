package com.mwong56.polyrides.utils;

import android.content.Intent;

/**
 * Created by micha on 10/12/2015.
 */
public interface OnActivityResultListener {
  void onActivityResultCalled(int requestCode, Intent data);
}
