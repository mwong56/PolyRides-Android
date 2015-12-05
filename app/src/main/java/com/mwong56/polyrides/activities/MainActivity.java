package com.mwong56.polyrides.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.adapters.TabAdapter;
import com.mwong56.polyrides.fragments.BaseTabbedFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseRxActivity implements GoogleApiClient.OnConnectionFailedListener {
  private static final String TAG = "MainActivity";

  @Bind(R.id.toolbar)
  Toolbar toolbar;

  @Bind(R.id.view_pager)
  ViewPager viewPager;

  @Bind(R.id.tab_layout)
  TabLayout tabLayout;


  private GoogleApiClient apiClient;
  private TabAdapter adapter;
  private int currentPosition = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    getSupportActionBar().setElevation(0);

    adapter = new TabAdapter(getSupportFragmentManager(), getBaseContext());
    viewPager.setAdapter(adapter);
    viewPager.addOnPageChangeListener(new OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        currentPosition = position;
        BaseTabbedFragment[] fragments = adapter.getFragments();
        for (int i = 0; i < fragments.length; i++) {
          if (position != i) {
            fragments[i].onHidden();
          } else {
            fragments[i].onVisible();
          }
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

    tabLayout.setupWithViewPager(viewPager);

    apiClient = new GoogleApiClient.Builder(getBaseContext())
        .enableAutoManage(this, 0, this)
        .addApi(Places.GEO_DATA_API)
        .build();
  }

  @Override
  protected void onResume() {
    super.onResume();
    adapter.getFragments()[currentPosition].onVisible();
  }

  @Override
  protected void onPause() {
    for (BaseTabbedFragment fragments : adapter.getFragments()) {
      fragments.onHidden();
    }
    super.onPause();
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (apiClient != null) {
      apiClient.connect();
    }
  }

  @Override
  protected void onStop() {
    if (apiClient != null && apiClient.isConnected()) {
      apiClient.disconnect();
    }
    super.onStop();
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
        + connectionResult.getErrorCode());

    // TODO(Developer): Check error code and notify the user of error state and resolution.
    Toast.makeText(getBaseContext(),
        "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
        Toast.LENGTH_SHORT).show();
  }

  public GoogleApiClient getGoogleApiClient() {
    return this.apiClient;
  }
}
