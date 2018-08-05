package project.files.android.addrequest.Analytics;

import com.appsee.Appsee;

import java.util.HashMap;

import project.files.android.addrequest.Utils.GlobalConstants;

public class AppseeFunctions {

    public static void saveTicket(int ticketType,
                                  final String userId,
                                  final String userName,
                                  final int ticketId,
                                  final String ticketTitle){

        if(ticketType == GlobalConstants.ADD_TICKET_TYPE){
            Appsee.addEvent("Ticket Added", new HashMap<String, Object>() {{
                put("User Id", userId);
                put("User Name", userName);
                put("Ticket Id",ticketId);
                put("Ticket Title",ticketTitle);
            }});
        } else {
            Appsee.addEvent("Ticket Updated", new HashMap<String, Object>() {{
                put("User Id", userId);
                put("User Name", userName);
                put("Ticket Id",ticketId);
                put("Ticket Title",ticketTitle);
            }});
        }

    }

}
