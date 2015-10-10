package com.mwong56.polyrides.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.adapters.TabAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

  @Bind(R.id.main_toolbar)
  Toolbar toolbar;

  @Bind(R.id.view_pager)
  ViewPager viewPager;

  @Bind(R.id.tab_layout)
  TabLayout tabLayout;

  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), getBaseContext());
    viewPager.setAdapter(adapter);

    tabLayout.setupWithViewPager(viewPager);
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
}
