package com.app.landlordcommunication.views.ChatScreen;

import com.app.landlordcommunication.Constants;
import com.app.landlordcommunication.async.base.SchedulerProvider;
import com.app.landlordcommunication.models.Message;
import com.app.landlordcommunication.models.MessagesCounter;
import com.app.landlordcommunication.services.message.base.MessageService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class ChatScreenPresenter implements ChatScreenContracts.Presenter{

    private final MessageService mMessageService;
    private final SchedulerProvider mSchedulerProvider;
    private ChatScreenContracts.View mView;

    @Inject
    public ChatScreenPresenter(MessageService mMessageService, SchedulerProvider mSchedulerProvider) {
        this.mMessageService = mMessageService;
        this.mSchedulerProvider = mSchedulerProvider;
    }

    @Override
    public void subscribe(ChatScreenContracts.View view) {
        mView = view;
    }

    @Override
    public void loadMessages() {
        mView.showLoading();
        Disposable observable = Observable.create((ObservableOnSubscribe<List<Message>>) emitter ->{
            List<Message> messages = mMessageService
                    .getAllMessagesBetweenReceiverAndSender(
                            Constants.CURRENT_USER_ID,
                            Constants.TEST_CHATTEE_USER_ID
                    );
            emitter.onNext(messages);
            emitter.onComplete();
        }).subscribeOn(mSchedulerProvider.background())
                .observeOn(mSchedulerProvider.ui())
                .doFinally(mView::hideLoading)
                .subscribe(
                        this::presentMessagesToView
                );

    }
    @Override
    public void sendMessage(Message message) {
        Disposable observable = Observable.create((ObservableOnSubscribe<Message>) emitter ->{
            Message testMessage = mMessageService.sendMessage(message);
            emitter.onNext(testMessage);
            emitter.onComplete();
        }).subscribeOn(mSchedulerProvider.background())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(
                        this::presentMessageToView
                );
    }

    @Override
    public void loadMessagesCount(){
        Disposable observable = Observable.create((ObservableOnSubscribe<MessagesCounter>) emitter ->{

            MessagesCounter counter = mMessageService.getMessageCount(
                    Constants.CURRENT_USER_ID,
                    Constants.TEST_CHATTEE_USER_ID
            );
            emitter.onNext(counter);
            emitter.onComplete();
        }).subscribeOn(mSchedulerProvider.background())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(
                        this::presentCount
                );
    }

    public void presentCount(MessagesCounter messagesCounter) {
        mView.showCount(messagesCounter);
    }

    private void presentMessageToView(Message message){
        mView.showMessage(message);
    }



    private void presentMessagesToView(List<Message> messages) {
        if(messages.isEmpty()){
            mView.showEmptyList();
        }else{
            mView.showMessages(messages);
        }
    }

    @Override
    public void selectMessage(Message message) {
        mView.showPicture(message);
    }


}
