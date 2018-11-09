package com.app.landlordcommunication.views.ChatScreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.landlordcommunication.Constants;
import com.app.landlordcommunication.R;
import com.app.landlordcommunication.models.Message;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChatScreenFragment extends Fragment implements ChatScreenContracts.View {

    @BindView(R.id.list)
    RecyclerView mMessagesView;

    @BindView(R.id.loading)
    ProgressBar mLoadingView;

    @BindView(R.id.material_design_android_floating_action_menu)
    FloatingActionMenu materialDesignFAM;

    @BindView(R.id.material_design_floating_action_menu_item1)
    FloatingActionButton mFabSorry;

    @BindView(R.id.material_design_floating_action_menu_item2)
    FloatingActionButton mFabSetUpMeeting;

    @BindView(R.id.material_design_floating_action_menu_item3)
    FloatingActionButton mFabHello;

    @Inject
    ChatScreenContracts.Presenter mPresenter;
    private GridLayoutManager mMessagesViewLayoutManager;

    @Inject
    MessagesAdapter mMessagesAdapter;

    @BindView(R.id.typeMessage_EditText)
    TextView mEditText;

    @Inject
    public ChatScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_screen, container, false);

        ButterKnife.bind(this, view);


        Intent intent = getActivity().getIntent();
        String chatteePicture = intent.getStringExtra("chatteePicture");
        InputStream chattePictureStream = new ByteArrayInputStream(Base64.decode(chatteePicture.getBytes(), Base64.DEFAULT));
        Bitmap chattePictureBitmap = BitmapFactory.decodeStream(chattePictureStream);
        mMessagesAdapter.setChatteePictureBitmap(chattePictureBitmap);
        String loggedPicture = intent.getStringExtra("chatterPicture");
        InputStream chatterPictureStream = new ByteArrayInputStream(Base64.decode(loggedPicture.getBytes(), Base64.DEFAULT));
        Bitmap chatterPictureBitmap = BitmapFactory.decodeStream(chatterPictureStream);
        mMessagesAdapter.setChatterPictureBitmap(chatterPictureBitmap);


        mMessagesView.setAdapter(mMessagesAdapter);
        mMessagesViewLayoutManager = new GridLayoutManager(getContext(), 1);
        mMessagesView.setLayoutManager(mMessagesViewLayoutManager);

        mEditText.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                createMessage(v.getText().toString());
                handled = true;
            }
            return handled;
        });

        mFabSorry.setOnClickListener(v -> createMessage("Sorry for the late reply."));
        mFabSetUpMeeting.setOnClickListener(v -> createMessage("Let's set up a meeting."));
        mFabHello.setOnClickListener(v -> createMessage("Hello, How are you?"));

        return view;
    }

    @Override
    public void createMessage(String messageText) {

        Message message = new Message();
        message.setReceiverId(Constants.TEST_CHATTEE_USER_ID);
        message.setSenderId(Constants.CURRENT_USER_ID);
        message.setResidenceId(Constants.TEST_RESIDENCE_ID);
        message.setText(messageText);
        Date sentDate = Calendar.getInstance().getTime();
        message.setSentDate(sentDate);

        //mMessagesAdapter.notifyDataSetChanged();

        mPresenter.sendMessage(message);
    }

    @Override
    public void showMessage(Message message) {
        mPresenter.loadMessages();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe(this);
        mPresenter.loadMessages();
    }


    @Override
    public void setPresenter(ChatScreenContracts.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMessages(List<Message> messages) {
        mMessagesAdapter.clear();
        mMessagesAdapter.addAll(messages);
        mMessagesAdapter.notifyDataSetChanged();
        mMessagesView.smoothScrollToPosition(mMessagesView.getAdapter().getItemCount() - 1);
    }

    @Override
    public void showEmptyList() {
        Toast.makeText(getContext(),
                "NO MESSAGES TO SHOW",
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showError(Throwable e) {
        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showLoading() {
        mMessagesView.setVisibility(View.GONE);
        mMessagesView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mMessagesView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
    }

}