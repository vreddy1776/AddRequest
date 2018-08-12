package project.files.android.addrequest.Analytics;

import com.appsee.Appsee;

import java.util.HashMap;

import project.files.android.addrequest.Utils.C;


/**
 * Appsee Functions
 *
 * Appsee analytics tools
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class AppseeFunctions {

    // Analytics taken when user logs in
    public static void login(final String userId, final String userName){

        Appsee.addEvent("User Sign-In", new HashMap<String, Object>() {{
            put("User Id", userId);
            put("User NameUtils", userName);
        }});

    }

    // Analytics taken when user creates or updates ticket
    public static void saveTicket(int ticketType,
                                  final String userId,
                                  final String userName,
                                  final int ticketId,
                                  final String ticketTitle){

        if(ticketType == C.ADD_TICKET_TYPE){
            Appsee.addEvent("Ticket Added", new HashMap<String, Object>() {{
                put("User Id", userId);
                put("User NameUtils", userName);
                put("Ticket Id",ticketId);
                put("Ticket Title",ticketTitle);
            }});
        } else {
            Appsee.addEvent("Ticket Updated", new HashMap<String, Object>() {{
                put("User Id", userId);
                put("User NameUtils", userName);
                put("Ticket Id",ticketId);
                put("Ticket Title",ticketTitle);
            }});
        }

    }

}
