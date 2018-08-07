package project.files.android.addrequest.Analytics;

import com.appsee.Appsee;

import java.util.HashMap;

import project.files.android.addrequest.Utils.GlobalConstants;


/**
 * Appsee Functions
 *
 * Appsee analytics tools
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class AppseeFunctions {


    public static void login(final String userId, final String userName){

        Appsee.addEvent("User Sign-In", new HashMap<String, Object>() {{
            put("User Id", userId);
            put("User Name", userName);
        }});

    }


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
