package com.mwong56.polyrides.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mwong56.polyrides.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FindRideActivityFragment extends Fragment {

  public FindRideActivityFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_recycler_view, container, false);
  }
}
