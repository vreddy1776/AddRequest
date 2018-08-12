package project.files.android.addrequest.Utils;


import java.math.BigDecimal;
import java.util.Random;


/**
 * ID Utils
 *
 * Generate and convert random Ticket IDs
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class IdUtils {

    public static int newID(){

        final int min = 100000000;
        final int max = 200000000;

        Random random = new Random();
        final int ID = random.nextInt((max - min) + 1) + min;

        return ID;

    }

    public static int convertID(String numberString){

        int result = 0;

        try {
            result = new BigDecimal(numberString).intValueExact();
        } catch (NumberFormatException e) {
            result = Integer.parseInt(numberString);
        }

        return result;

    }

}
