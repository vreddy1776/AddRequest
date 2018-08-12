package project.files.android.addrequest.Activity.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import project.files.android.addrequest.Adapter.MessageAdapter;
import project.files.android.addrequest.R;
import project.files.android.addrequest.Utils.C;


/**
 * Chat Activity
 *
 * Chat service that uses Firebase instead of local DB
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class ChatActivity extends AppCompatActivity implements ChatContract.View{


    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private EditText mMessageEditText;
    private Button mSendButton;

    private ChatContract.Presenter mPresenter;

    private String mTicketId;
    private String mTicketTitle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        mTicketId = intent.getStringExtra(C.TICKET_ID_KEY);
        mTicketTitle = intent.getStringExtra(C.TICKET_TITLE_KEY);

        getSupportActionBar().setTitle(mTicketTitle);

        mPresenter = new ChatPresenter(this, mTicketId);

        // Initialize references to views
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);

        // Initialize message ListView and its adapter
        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, mPresenter.createMessageList());
        mMessageListView.setAdapter(mMessageAdapter);

        // Enable Send button when there's text to send
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPresenter.sendMessage(getApplicationContext());
                mMessageEditText.setText("");
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        addMessageListener();
    }


    @Override
    protected void onStop() {
        super.onStop();
        removeMessageListener();
    }


    @Override
    public String getMessageText() {
        return mMessageEditText.getText().toString();
    }


    @Override
    public MessageAdapter getMessageAdapter() {
        return mMessageAdapter;
    }

    private void addMessageListener() {
        mPresenter.attachDatabaseReadListener();
    }


    private void removeMessageListener() {
        mPresenter.detachDatabaseReadListener();
    }


}

