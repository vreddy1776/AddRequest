package project.files.android.addrequest.Utils;

import java.util.Random;


/**
 * String Utils
 *
 * Generate random title and description strings for tests
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class StringUtils {

    public static String randomString(int length) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(length);
        char tempChar;
        for (int i = 0; i < length; i++){
            tempChar = (char) (generator.nextInt(96) + 31);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


    public static String randomTitle(){

        String newTitle =
                StringUtils.randomString(3) +
                        "-Title-" +
                        StringUtils.randomString(3);

        return newTitle;
    }


    public static String randomDescription(){

        String newDescription =
                StringUtils.randomString(5) +
                        "-Description-" +
                        StringUtils.randomString(5);

        return newDescription;
    }

}
