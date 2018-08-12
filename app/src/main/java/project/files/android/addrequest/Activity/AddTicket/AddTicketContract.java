package project.files.android.addrequest.Activity.AddTicket;

import android.content.Context;

import java.util.List;

import project.files.android.addrequest.Activity.Chat.Message;
import project.files.android.addrequest.Adapter.MessageAdapter;


/**
 * AddTicket Contract
 *
 * Contains view interface for AddTicket viewModel
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class AddTicketContract {

    interface View {

        void updateTitleDescription();

        void setVideoView();

    }

}
