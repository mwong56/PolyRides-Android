package com.mwong56.polyrides.fragments;

import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by micha on 10/22/2015.
 */
public abstract class BaseRxFragment extends RxFragment {

  protected void showToast(Throwable e) {
    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
  }
}
