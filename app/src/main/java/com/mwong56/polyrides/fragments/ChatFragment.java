package com.mwong56.polyrides.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.MessageActivity;
import com.mwong56.polyrides.models.Chat;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;
import com.mwong56.polyrides.views.ChatViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.ribot.easyadapter.EasyRecyclerAdapter;

/**
 * Created by micha on 10/9/2015.
 */
public class ChatFragment extends BaseRxFragment implements ChatViewHolder.ChatListener {

  @Bind(R.id.recycler_view)
  RecyclerView recyclerView;

  private final List<Chat> chatList = new ArrayList<>();
  private final PolyRidesService polyRidesService = PolyRidesServiceImpl.get();
  private EasyRecyclerAdapter<Chat> adapter;

  public static ChatFragment newInstance() {
    return new ChatFragment();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    this.adapter = new EasyRecyclerAdapter<>(getContext(), ChatViewHolder.class, chatList, this);
    this.recyclerView.setAdapter(adapter);

    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);

    polyRidesService.getChats()
        .compose(bindToLifecycle())
        .subscribe(chats -> {
          chatList.addAll(chats);
          adapter.notifyDataSetChanged();
        }, error -> showToast(error));
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_reycler_view, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onChatClicked(Chat chat) {
    Intent i = new Intent(getActivity(), MessageActivity.class);
    i.putExtra("groupId", chat.getGroupId());
    startActivity(i);
  }
}
