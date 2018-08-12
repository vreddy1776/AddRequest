package project.files.android.addrequest.Activity.Chat;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import project.files.android.addrequest.Settings.UserProfileSettings;
import project.files.android.addrequest.Utils.C;




/**
 * Chat Presenter
 *
 * Presenter for data to ChatActivity
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class ChatPresenter implements ChatContract.Presenter {


    @NonNull
    private final ChatContract.View mView;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;


    public ChatPresenter(@NonNull ChatContract.View view, String ticketId){
        mView = view;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference()
                .child(C.CHILD_NAME_TICKETS)
                .child(ticketId)
                .child(C.CHILD_NAME_MESSAGES);
    }


    @Override
    public List<Message> createMessageList() {
        List<Message> messages = new ArrayList<>();
        return messages;
    }


    @Override
    public void sendMessage(Context context) {

        Message message = new Message(mView.getMessageText(),
                UserProfileSettings.getUsername(context),
                UserProfileSettings.getUserPhotoURL(context));
        mMessagesDatabaseReference.push().setValue(message);

    }


    @Override
    public void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Message message = dataSnapshot.getValue(Message.class);
                    mView.getMessageAdapter().add(message);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }


    @Override
    public void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

}
