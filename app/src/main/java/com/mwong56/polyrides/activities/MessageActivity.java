package com.mwong56.polyrides.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.adapters.MessageListAdapter;
import com.mwong56.polyrides.models.Message;
import com.mwong56.polyrides.models.Messages;
import com.mwong56.polyrides.models.User;
import com.mwong56.polyrides.services.FacebookService;
import com.mwong56.polyrides.services.FacebookServiceImpl;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;
import com.mwong56.polyrides.views.SendButton;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    private Messages messages;
    private String otherUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        facebookService.getUserName(AccessToken.getCurrentAccessToken(), this.messages.getOtherUserId())
                .subscribe(userName -> setTitle(userName), onError -> showToast(onError));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);


        this.messages = Parcels.unwrap(getIntent().getParcelableExtra("messages"));
        this.adapter = new MessageListAdapter(getBaseContext(), messageList);
        this.recyclerView.setAdapter(adapter);
        this.sendButton.setOnSendClickListener(v -> onSendClick());
        polyRidesService.getMessage(messages.getGroupId())
                .compose(bindToLifecycle())
                .subscribe(messages -> {
                    messageList.addAll(messages);
                    adapter.notifyDataSetChanged();
                }, error -> showToast(error));
    }

    void onSendClick() {
        if (validateMessage()) {
            String text = messageText.getText().toString();
            Message message = new Message(messages.getGroupId(), User.getUserId(), text, User.getUserName());
            updateDatabase(message);
            adapter.addMessage(message);
            messageText.setText(null);
            sendButton.setCurrentState(SendButton.STATE_DONE);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        this.otherUser = title.toString();
    }

    void updateDatabase(Message message) {
        polyRidesService.saveMessage(message)
                .subscribe(onNext -> {
                }, onError -> showToast(onError));
        messages.setLastMessage(message.getText());
        messages.setLastUserId(User.getUserId());

        if (messages.isNewMessages()) {
            polyRidesService.createMessages(messages, User.getUserId()).subscribe(v -> {
            }, e -> showToast(e));
            polyRidesService.createMessages(messages, messages.getOtherUserId()).subscribe(v -> {
            }, e -> showToast(e));
            messages.setIsNewMessage(false);
        } else {
            polyRidesService.updateMessages(messages, User.getUserId()).subscribe(v -> {
            }, e -> showToast(e));
            polyRidesService.updateMessages(messages, messages.getOtherUserId()).subscribe(v -> {
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
