package com.mwong56.polyrides.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.adapters.TabAdapter;
import com.mwong56.polyrides.models.User;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseSessionActivity implements GoogleApiClient.OnConnectionFailedListener {

  @Bind(R.id.toolbar)
  Toolbar toolbar;

  @Bind(R.id.view_pager)
  ViewPager viewPager;

  @Bind(R.id.tab_layout)
  TabLayout tabLayout;

  private static final String TAG = "MainActivity";
  private GoogleApiClient apiClient;
  private TabAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    Crashlytics.setUserName(User.getUserName());
    Crashlytics.setUserIdentifier(User.getUserId());
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    getSupportActionBar().setElevation(0);

    adapter = new TabAdapter(getSupportFragmentManager(), getBaseContext());
    viewPager.setAdapter(adapter);
    tabLayout.setupWithViewPager(viewPager);

    apiClient = new GoogleApiClient.Builder(getBaseContext())
        .enableAutoManage(this, 0, this)
        .addApi(Places.GEO_DATA_API)
        .build();
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
