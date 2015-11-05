package com.mwong56.polyrides.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.adapters.MessageListAdapter;
import com.mwong56.polyrides.application.PolyRidesApp;
import com.mwong56.polyrides.models.Chat;
import com.mwong56.polyrides.models.Message;
import com.mwong56.polyrides.models.User;
import com.mwong56.polyrides.services.FacebookService;
import com.mwong56.polyrides.services.FacebookServiceImpl;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;
import com.mwong56.polyrides.utils.DoNothingOnNextAction;
import com.mwong56.polyrides.utils.Utils;
import com.mwong56.polyrides.views.SendButton;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * Created by micha on 10/25/2015.
 */
public class MessageActivity extends BaseRxActivity {

  private static final DoNothingOnNextAction doNothingOnNextAction = new DoNothingOnNextAction();
  private static final String TAG = MessageActivity.TAG;

  @Bind(R.id.recycler_view)
  RecyclerView recyclerView;

  @Bind(R.id.toolbar)
  Toolbar toolbar;

  @Bind(R.id.message_edit_text)
  EditText messageText;

  @Bind(R.id.btn_send)
  SendButton sendButton;

  private final PolyRidesService polyRidesService = PolyRidesServiceImpl.get();
  private final FacebookService facebookService = FacebookServiceImpl.get();
  private final List<Message> messageList = new ArrayList<>();
  private MessageListAdapter adapter;
  private String groupId;
  private String otherId;
  private String otherUserName;

  private Chat chat = null;

  private final ParsePushBroadcastReceiver receiver = new ParsePushBroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      try {
        JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
        String groupId = json.getString("groupId");

        if (groupId == null) {
          return;
        }

        if (MessageActivity.this.groupId.equals(groupId)) {
          refreshMessages();
        }

      } catch (Exception e) {
        Log.e(TAG, e.toString());
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_messages);
    ButterKnife.bind(this);

    this.groupId = getIntent().getStringExtra("groupId");
    this.otherId = Utils.extractOtherUserId(this.groupId);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    facebookService.getUserName(AccessToken.getCurrentAccessToken(), this.otherId)
        .subscribe(userName -> setTitle(userName), onError -> showToast(onError));

    setupViews();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    ((PolyRidesApp) getApplication()).setMessageGroupIdInForeground(this.groupId);
    this.registerReceiver(receiver, Utils.buildParseIntentFilter());
    polyRidesService.clearMessagesCounter(this.groupId, User.getUserId())
        .subscribe(v -> {}, error -> {});
    refreshMessages();
  }

  @Override
  protected void onStop() {
    this.unregisterReceiver(receiver);
    ((PolyRidesApp) getApplication()).setMessageGroupIdInForeground(null);
    super.onStop();
  }

  private void refreshMessages() {
    Observable.zip(polyRidesService.getMessage(this.groupId),
        polyRidesService.getChat(this.groupId),
        (messages, chat) -> Pair.create(messages, chat))
        .compose(bindToLifecycle())
        .subscribe(pair -> {
          messageList.clear();
          messageList.addAll(pair.first);
          adapter.notifyDataSetChanged();
          this.chat = pair.second;
        }, error -> {
          this.chat = null;
        });
  }

  private void setupViews() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    layoutManager.setStackFromEnd(true);
    recyclerView.setLayoutManager(layoutManager);

    this.adapter = new MessageListAdapter(getBaseContext(), messageList);
    this.recyclerView.setAdapter(adapter);
    this.sendButton.setOnSendClickListener(v -> onSendClick());
  }

  void onSendClick() {
    if (validateMessage()) {
      String text = messageText.getText().toString();
      Message message = new Message(this.groupId, User.getUserId(), text, User.getUserName());
      updateDatabase(message);
      adapter.addMessage(message);
      recyclerView.scrollToPosition(messageList.size() - 1);
      messageText.setText(null);
      sendButton.setCurrentState(SendButton.STATE_DONE);
    }
  }

  @Override
  public void setTitle(CharSequence title) {
    super.setTitle(title);
    this.otherUserName = title.toString();
  }

  void updateDatabase(Message message) {
    Observable.mergeDelayError(polyRidesService.saveMessage(message),
        polyRidesService.sendPush(this.otherId, User.getUserName(),
            this.groupId, message.getText()))
            .subscribe(doNothingOnNextAction, onError -> showToast(onError));

    if (this.chat == null) {
      this.chat = new Chat(this.groupId, message.getText(), User.getUserId());
      Observable.mergeDelayError(
          polyRidesService.createChat(chat, User.getUserId(), chat.getOtherUserId(), this.otherUserName),
          polyRidesService.createChat(chat, chat.getOtherUserId(), User.getUserId(), User.getUserName()))
          .subscribe(doNothingOnNextAction, e -> showToast(e));
    } else {
      this.chat.setLastMessage(message.getText());
      this.chat.setLastUserId(User.getUserId());
      Observable.mergeDelayError(
          polyRidesService.updateChat(chat, User.getUserId()),
          polyRidesService.updateChat(chat, chat.getOtherUserId()))
          .subscribe(doNothingOnNextAction, e -> showToast(e));
    }
  }

  private boolean validateMessage() {
    if (TextUtils.isEmpty(messageText.getText())) {
      sendButton.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.shake_error));
      return false;
    }
    return true;
  }

}
