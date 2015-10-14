package com.mwong56.polyrides.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.login.widget.LoginButton;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.json.JSONException;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by micha on 10/13/2015.
 */
public class LoginActivity extends RxAppCompatActivity {
  private static final String TAG = "LoginActivity";

  @Bind(R.id.login)
  LoginButton loginButton;

  private final PolyRidesService service = PolyRidesServiceImpl.get();

  @Override
  public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
    super.onCreate(savedInstanceState, persistentState);

    if (isExistingUser()) {
      startMainActivity();
    }

    setContentView(R.layout.activity_login);
  }

  @OnClick(R.id.login)
  void onLoginClicked() {
    service.facebookLogin(this, Arrays.asList("public_profile","user_friends"))
        .subscribe(user -> {
          if (user == null) {
            Log.d(TAG, "User canceled fb login");
          } else {
            updateParseUserInfoInBackground();
            startMainActivity();
          }
        });


  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
  }

  private boolean isExistingUser() {
    ParseUser user = ParseUser.getCurrentUser();
    return user != null && ParseFacebookUtils.isLinked(user);
  }

  private void startMainActivity() {
    Intent i = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(i);
    finish();
  }


  private void updateParseUserInfoInBackground(){
    GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (jsonObject, graphResponse) -> {
      if (jsonObject != null) {
        String userId;
        String userName;
        try {
          userId = jsonObject.getString("id");
          userName = jsonObject.getString("name");
        } catch (JSONException e) {
          Toast.makeText(getBaseContext(), "Error saving user", Toast.LENGTH_SHORT).show();
          return;
        }
        service.saveParseuserInfo(userId, userName);
      }
    });
  }
}
