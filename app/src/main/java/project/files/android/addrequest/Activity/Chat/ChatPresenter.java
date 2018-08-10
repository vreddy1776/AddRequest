package project.files.android.addrequest.Activity.Chat;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import project.files.android.addrequest.Adapter.MessageAdapter;
import project.files.android.addrequest.Settings.UserProfileSettings;
import project.files.android.addrequest.Utils.GlobalConstants;




public class ChatPresenter implements ChatContract.Presenter {


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;


    public ChatPresenter(String ticketId){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference()
                .child(GlobalConstants.CHILD_NAME_TICKETS)
                .child(ticketId)
                .child(GlobalConstants.CHILD_NAME_MESSAGES);
    }


    @Override
    public void pushMessage(Context context, String messageText) {

        Message message = new Message(messageText, UserProfileSettings.getUsername(context), null);
        mMessagesDatabaseReference.push().setValue(message);

    }


    @Override
    public void attachDatabaseReadListener(final MessageAdapter messageAdapter) {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messageAdapter.add(message);
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
