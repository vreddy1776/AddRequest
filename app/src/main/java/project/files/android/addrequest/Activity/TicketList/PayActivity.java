package project.files.android.addrequest.Activity.TicketList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;

import project.files.android.addrequest.R;

public class PayActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        CardInputWidget mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);

        Card cardToSave = mCardInputWidget.getCard();
        if (cardToSave == null) {
            //mErrorDialogHandler.showError("Invalid Card Data");
        }

        //cardToSave.setName("Customer Name");
        //cardToSave.setAddressZip("12345");

    }
}
