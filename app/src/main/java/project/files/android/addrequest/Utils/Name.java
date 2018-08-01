package project.files.android.addrequest.Utils;

public class Name {


    public static String getFirstName(String fullname) {

        String firstname = "";
        String middlename = "";
        String lastname = "";

        int start = fullname.indexOf(' ');
        int end = fullname.lastIndexOf(' ');

        if (start >= 0) {
            firstname = fullname.substring(0, start);
            if (end > start)
                middlename = fullname.substring(start + 1, end);
            lastname = fullname.substring(end + 1, fullname.length());
        }

        return firstname;

    }

    public static String getMiddleName(String fullname) {

        String firstname = "";
        String middlename = "";
        String lastname = "";

        int start = fullname.indexOf(' ');
        int end = fullname.lastIndexOf(' ');

        if (start >= 0) {
            firstname = fullname.substring(0, start);
            if (end > start)
                middlename = fullname.substring(start + 1, end);
            lastname = fullname.substring(end + 1, fullname.length());
        }

        return middlename;

    }

    public static String getLastName(String fullname) {

        String firstname = "";
        String middlename = "";
        String lastname = "";

        int start = fullname.indexOf(' ');
        int end = fullname.lastIndexOf(' ');

        if (start >= 0) {
            firstname = fullname.substring(0, start);
            if (end > start)
                middlename = fullname.substring(start + 1, end);
            lastname = fullname.substring(end + 1, fullname.length());
        }

        return lastname;

    }

}
