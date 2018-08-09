package project.files.android.addrequest.Activity.Chat;

import android.support.annotation.NonNull;

import java.io.IOException;

public class ChatContract {

    interface View {

        void showEmptyNoteError();

        void showNotesList();

        void openCamera(String saveTo);

        void showImagePreview(@NonNull String uri);

        void showImageError();
    }

    interface Presenter {

        void setDatabaseReference();

        void pushMessage(String messageText);


        void saveNote(String title, String description);

        void takePicture() throws IOException;

        void imageAvailable();

        void imageCaptureFailed();
    }

}
