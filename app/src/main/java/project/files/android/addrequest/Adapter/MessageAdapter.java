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

import project.files.android.addrequest.Activity.Chat.Message;
import project.files.android.addrequest.R;
import project.files.android.addrequest.Utils.NameUtils;


/**
 * Message Adapter
 *
 * {@link MessageAdapter} exposes items to {@link project.files.android.addrequest.Activity.Chat.ChatActivity}
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
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

        boolean isPhoto = false;
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

        authorTextView.setText(NameUtils.getFirstName(message.getName()));
        Glide.with(getContext())
                .load(message.getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(profilePicView);

        return convertView;
    }
}
