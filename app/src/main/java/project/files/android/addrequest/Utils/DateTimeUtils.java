package project.files.android.addrequest.Utils;

import android.arch.persistence.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * DateTime Utils
 *
 * Convert and format Date object to string
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class DateTimeUtils {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


    public static Date stringToDate(String dateString){

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }


    public static String dateToString(Date date){

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-YYYY  h:mm a");
        String dateString = sdf.format(date);
        return dateString;

    }


}
