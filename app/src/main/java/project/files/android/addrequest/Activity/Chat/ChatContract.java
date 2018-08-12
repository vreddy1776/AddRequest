package project.files.android.addrequest.Activity.Chat;

import android.content.Context;

import java.util.List;

import project.files.android.addrequest.Adapter.MessageAdapter;


/**
 * Chat Contract
 *
 * Presenter interface for ChatActivity and View interface for ChatPresenter
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class ChatContract {

    interface View {

        String getMessageText();

        MessageAdapter getMessageAdapter();

    }

    interface Presenter {

        List<Message> createMessageList();

        void sendMessage(Context context);

        void attachDatabaseReadListener();

        void detachDatabaseReadListener();

    }

}
