package com.mwong56.polyrides.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.User;
import com.mwong56.polyrides.services.FacebookService;
import com.mwong56.polyrides.services.FacebookServiceImpl;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;
import com.mwong56.polyrides.utils.DoNothingOnNextAction;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by micha on 10/13/2015.
 */
public class LoginActivity extends BaseRxActivity {
  private static final String TAG = "LoginActivity";

  @Bind(R.id.login)
  LoginButton loginButton;

  private final PolyRidesService polyRidesService = PolyRidesServiceImpl.get();
  private final FacebookService fbService = FacebookServiceImpl.get();

  private CallbackManager callbackManager;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (User.getUserId() != null) {
      startMainActivity();
      return;
    }

    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);

    callbackManager = CallbackManager.Factory.create();
    loginButton.setReadPermissions("public_profile", "user_friends");
    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override
      public void onSuccess(LoginResult loginResult) {
        getUserIdAndUsername()
            .subscribe(pair -> {
              polyRidesService.saveUserId(pair.first)
                  .subscribe(new DoNothingOnNextAction(), error -> showToast(error));
              User.setUserId(pair.first);
              User.setUserName(pair.second);
              startMainActivity();
            }, error -> showToast(error));
      }

      @Override
      public void onCancel() {
        return;
      }

      @Override
      public void onError(FacebookException exception) {
        showToast(exception);
      }
    });

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }

  private void startMainActivity() {
    Intent i = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(i);
    finish();
  }

  Observable<Pair<String, String>> getUserIdAndUsername() {
    return Observable.zip(fbService.getMyUserId(AccessToken.getCurrentAccessToken()),
        fbService.getMyUserName(AccessToken.getCurrentAccessToken()), (s, s2) -> Pair.create(s, s2))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.newThread())
        .compose(bindToLifecycle());
  }
}
