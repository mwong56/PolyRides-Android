package com.mwong56.polyrides.activities;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.mwong56.polyrides.models.User;

/**
 * Created by Michael on 12/16/2015.
 */
public class BaseSessionActivity extends BaseRxActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    validateSession();
  }

  @Override
  protected void onStart() {
    super.onStart();
    validateSession();
  }

  private void validateSession() {
    if (!validSession()) {
      showToast("Invalid session");
      LoginManager.getInstance().logOut();
      User.logout();
      Intent i = new Intent(getBaseContext(), LoginActivity.class);
      startActivity(i);
      finish();
    }
  }

  private boolean validSession() {
    if (User.getUserId() == null || User.getUserId().length() == 0) {
      return false;
    }
    return AccessToken.getCurrentAccessToken() != null;
  }
}
