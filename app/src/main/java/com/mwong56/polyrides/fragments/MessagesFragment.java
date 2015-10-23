package com.mwong56.polyrides.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mwong56.polyrides.R;

/**
 * Created by micha on 10/9/2015.
 */
public class MessagesFragment extends Fragment {

  public static MessagesFragment newInstance() {
    return new MessagesFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_messages, container, false);
  }
}
