package project.files.android.addrequest.Activity.Chat;

import android.content.Context;

import project.files.android.addrequest.Adapter.MessageAdapter;

public class ChatContract {

    interface View {

        void addMessageListener();

        void removeMessageListener();

    }

    interface Presenter {

        void pushMessage(Context context, String messageText);

        void attachDatabaseReadListener(MessageAdapter messageAdapter);

        void detachDatabaseReadListener();

    }

}
