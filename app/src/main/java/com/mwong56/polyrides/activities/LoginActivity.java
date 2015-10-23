package com.mwong56.polyrides.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.facebook.AccessToken;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.User;
import com.mwong56.polyrides.services.FacebookService;
import com.mwong56.polyrides.services.FacebookServiceImpl;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;
import com.parse.ParseFacebookUtils;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by micha on 10/13/2015.
 */
public class LoginActivity extends BaseRxActivity {
  private static final String TAG = "LoginActivity";

  @Bind(R.id.login)
  Button loginButton;

  private final PolyRidesService polyRidesService = PolyRidesServiceImpl.get();
  private final FacebookService fbService = FacebookServiceImpl.get();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (User.getUserId() != null) {
      startMainActivity();
    }

    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.login)
  void onLoginClicked() {
    polyRidesService.facebookLogin(this, Arrays.asList("public_profile", "user_friends"))
        .flatMap(parseUser -> fbService.getMyUserId(AccessToken.getCurrentAccessToken()))
        .subscribe(user -> {
          polyRidesService.saveUserId(user);
          User.setUserId(user);
          startMainActivity();
        }, error -> showToast(error));
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
  }

  private void startMainActivity() {
    Intent i = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(i);
    finish();
  }


}
