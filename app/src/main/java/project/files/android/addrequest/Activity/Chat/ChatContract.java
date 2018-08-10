package project.files.android.addrequest.Activity.Chat;

import android.content.Context;

import project.files.android.addrequest.Adapter.MessageAdapter;

public class ChatContract {

    interface View {

        String getMessageText();

    }

    interface Presenter {

        void sendMessage(Context context);

        void attachDatabaseReadListener(MessageAdapter messageAdapter);

        void detachDatabaseReadListener();

    }

}
