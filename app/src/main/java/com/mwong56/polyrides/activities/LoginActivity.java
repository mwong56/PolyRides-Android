package com.mwong56.polyrides.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.User;
import com.mwong56.polyrides.services.FacebookService;
import com.mwong56.polyrides.services.FacebookServiceImpl;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;
import com.parse.ParseFacebookUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by micha on 10/13/2015.
 */
public class LoginActivity extends RxAppCompatActivity {
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
//    progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait", "Logging in...", true);
    polyRidesService.facebookLogin(this, Arrays.asList("public_profile", "user_friends"))
        .subscribe(user -> {
          updateParseUserInfoInBackground();
        }, error -> showToast(error));
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
  }

  private void startMainActivity() {
//    progressDialog.dismiss();
    Intent i = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(i);
    finish();
  }


  private void updateParseUserInfoInBackground() {
    fbService.getUserId(AccessToken.getCurrentAccessToken())
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(Schedulers.newThread())
        .subscribe(userId -> {
          User.setUserId(userId);
          polyRidesService.saveParseuserInfo(userId);
          startMainActivity();
        }, error -> showToast(error));
  }

  private void showToast(Throwable e) {
    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
  }
}
