package project.files.android.addrequest.Background;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.parceler.Parcels;

import project.files.android.addrequest.Database.AppDatabase;
import project.files.android.addrequest.Database.Ticket;
import project.files.android.addrequest.Utils.C;


/**
 * Video Upload Service
 *
 * Service to run if user uploads video.
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class VideoUploadService extends Service {


    private static final String TAG = VideoUploadService.class.getSimpleName();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Ticket ticket = (Ticket) Parcels.unwrap(intent.getParcelableExtra(C.TICKET_KEY));
        int ticketType = (int) intent.getExtras().get(C.TICKET_TYPE_KEY);

        uploadVideo(ticket,ticketType);

        return super.onStartCommand(intent, flags, startId);
    }


    private void uploadVideo(final Ticket tempTicket, final int ticketType){

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference firebaseVideoRef = firebaseStorage.getReference().child("Videos");

        Uri capturedVideoUri = Uri.parse(tempTicket.getTicketVideoLocalUri());
        StorageReference localVideoRef = firebaseVideoRef.child(capturedVideoUri.getLastPathSegment());
        UploadTask uploadTask = localVideoRef.putFile(capturedVideoUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                stopSelf();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                tempTicket.setTicketVideoPostId(C.VIDEO_EXISTS_TICKET_VIDEO_POST_ID);
                tempTicket.setTicketVideoInternetUrl(taskSnapshot.getDownloadUrl().toString());

                addTicketToDb(tempTicket, ticketType);

                Notifications.ticketPostedNotification(tempTicket.getTicketId());

                stopSelf();
            }
        });

    }


    public void addTicketToDb(final Ticket ticket, final int ticketType){

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                addTicketToLocalDb(ticket,ticketType);
            }
        });
        AppExecuters.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                addTicketToFirebaseDb(ticket);
            }
        });
    }


    public void addTicketToLocalDb(final Ticket ticket, int ticketType){

        if(ticketType == C.ADD_TICKET_TYPE){
            AppDatabase.getInstance().ticketDao().insertTicket(ticket);
        } else {
            AppDatabase.getInstance().ticketDao().updateTicket(ticket);
        }
    }


    private void addTicketToFirebaseDb(final Ticket ticket){

        FirebaseDatabase fBdatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = fBdatabase.getReference("Tickets");
        myRef.child(String.valueOf(ticket.getTicketId())).setValue(ticket);

    }



}
