package com.mwong56.polyrides.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListView;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.views.SendButton;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by micha on 10/13/2015.
 */
public class ChatFragment extends Fragment implements SendButton.OnSendClickListener  {

  @Bind(R.id.list)
  ListView listView;

  @Bind(R.id.message_edit_text)
  EditText messageText;

  @Bind(R.id.btn_send_comment)
  SendButton sendButton;

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    sendButton.setOnSendClickListener(this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_chat, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onSendClickListener(View v) {
    if (validateMessage()) {
      String text = messageText.getText().toString();
      //TODO: Add to adapter, save message to DB.
      messageText.setText(null);
      sendButton.setCurrentState(SendButton.STATE_DONE);
    }
  }

  private boolean validateMessage() {
    if (TextUtils.isEmpty(messageText.getText())) {
      sendButton.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_error));
      return false;
    }

    return true;
  }
}
