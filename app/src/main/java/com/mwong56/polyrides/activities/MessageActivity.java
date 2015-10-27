package com.mwong56.polyrides.activities;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.adapters.MessageListAdapter;
import com.mwong56.polyrides.models.Chat;
import com.mwong56.polyrides.models.Message;
import com.mwong56.polyrides.models.User;
import com.mwong56.polyrides.services.FacebookService;
import com.mwong56.polyrides.services.FacebookServiceImpl;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;
import com.mwong56.polyrides.utils.Utils;
import com.mwong56.polyrides.views.SendButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * Created by micha on 10/25/2015.
 */
public class MessageActivity extends BaseRxActivity {

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

  private Chat chat;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_messages);
    ButterKnife.bind(this);

    this.groupId = getIntent().getStringExtra("groupId");
    this.otherId = Utils.extractOtherUserId(this.groupId);

    setSupportActionBar(toolbar);
    facebookService.getUserName(AccessToken.getCurrentAccessToken(), this.otherId)
        .subscribe(userName -> setTitle(userName), onError -> showToast(onError));

    setupViews();

    Observable.zip(polyRidesService.getMessage(this.groupId),
        polyRidesService.getChat(this.groupId),
        (messages, chat) -> Pair.create(messages, chat))
        .compose(bindToLifecycle())
        .subscribe(pair -> {
          messageList.addAll(pair.first);
          adapter.notifyDataSetChanged();
          this.chat = pair.second;
        }, error -> showToast(error));
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
      Message message = new Message(chat.getGroupId(), User.getUserId(), text, User.getUserName());
      updateDatabase(message);
      adapter.addMessage(message);
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
    polyRidesService.saveMessage(message)
        .subscribe(onNext -> {},
            onError -> showToast(onError));

    if (this.chat == null) {
      this.chat = new Chat(1, this.otherUserName, this.groupId, message.getText(), User.getUserId());
      polyRidesService.createMessages(chat, User.getUserId()).subscribe(v -> {
      }, e -> showToast(e));
      polyRidesService.createMessages(chat, chat.getOtherUserId()).subscribe(v -> {
      }, e -> showToast(e));
    } else {
      this.chat.setCounter(chat.getCounter() + 1);
      this.chat.setLastMessage(chat.getLastMessage());
      this.chat.setLastUserId(User.getUserId());
      polyRidesService.updateMessages(chat, User.getUserId()).subscribe(v -> {
      }, e -> showToast(e));
      polyRidesService.updateMessages(chat, chat.getOtherUserId()).subscribe(v -> {
      }, e -> showToast(e));
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
