package com.mwong56.polyrides.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.Messages;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;
import com.mwong56.polyrides.views.MessagesViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.ribot.easyadapter.EasyRecyclerAdapter;

/**
 * Created by micha on 10/9/2015.
 */
public class MessagesFragment extends BaseRxFragment {

  @Bind(R.id.recycler_view)
  RecyclerView recyclerView;

  private final List<Messages> messagesList = new ArrayList<>();
  private final PolyRidesService polyRidesService = PolyRidesServiceImpl.get();
  private EasyRecyclerAdapter<Messages> adapter;

  public static MessagesFragment newInstance() {
    return new MessagesFragment();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    this.adapter = new EasyRecyclerAdapter<>(getContext(), MessagesViewHolder.class, messagesList, this);
    this.recyclerView.setAdapter(adapter);

    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);

    polyRidesService.getMessages()
        .compose(bindToLifecycle())
        .subscribe(messages -> {
          messagesList.addAll(messages);
          adapter.notifyDataSetChanged();
        }, error -> showToast(error));
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
    ButterKnife.bind(this, view);
    return view;
  }
}
