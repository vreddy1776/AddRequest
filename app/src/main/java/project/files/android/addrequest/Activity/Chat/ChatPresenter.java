package project.files.android.addrequest.Activity.Chat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import project.files.android.addrequest.Settings.UserProfileSettings;
import project.files.android.addrequest.Utils.GlobalConstants;

public class ChatPresenter  {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;

    /*

    @Override
    public void setDatabaseReference() {

        mMessagesDatabaseReference = mFirebaseDatabase.getReference()
                .child(GlobalConstants.CHILD_NAME_TICKETS)
                .child(mTicketId)
                .child(GlobalConstants.CHILD_NAME_MESSAGES);

    }

    @Override
    public void pushMessage(String messageText) {

        Message message = new Message(messageText, UserProfileSettings.getUsername(this), null);
        mMessagesDatabaseReference.push().setValue(message);

    }
    */

}
