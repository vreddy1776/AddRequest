package project.files.android.addrequest.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import project.files.android.addrequest.MVVM.Chat.Message;
import project.files.android.addrequest.R;
import project.files.android.addrequest.Settings.UserProfileSettings;
import project.files.android.addrequest.Utils.Name;


/**
 * {@link MessageAdapter} exposes items to {@link project.files.android.addrequest.MVVM.Chat.ChatActivity}
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        ImageView profilePicView = (ImageView) convertView.findViewById(R.id.messageProfilePic) ;

        Message message = getItem(position);

        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);
        } else {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getText());
        }
        authorTextView.setText(Name.getFirstName(message.getName()));
        Glide.with(getContext())
                .load(UserProfileSettings.getUserPhotoURL(getContext()))
                .apply(RequestOptions.circleCropTransform())
                .into(profilePicView);

        return convertView;
    }
}
