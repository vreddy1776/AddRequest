package project.files.android.addrequest.Utils;

import java.util.Random;

public class StringGenerator {

    public static String randomString(int length) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(length);
        char tempChar;
        for (int i = 0; i < length; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


    public static String randomTitle(){

        String newTitle =
                StringGenerator.randomString(3) +
                        "-Title-" +
                        StringGenerator.randomString(3);

        return newTitle;
    }


    public static String randomDescription(){

        String newDescription =
                StringGenerator.randomString(5) +
                        "-Description-" +
                        StringGenerator.randomString(5);

        return newDescription;
    }

}
