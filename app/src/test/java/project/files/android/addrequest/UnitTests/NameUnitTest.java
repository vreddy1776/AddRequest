package project.files.android.addrequest.UnitTests;

import org.junit.Test;

import project.files.android.addrequest.Utils.NameUtils;

import static org.junit.Assert.assertEquals;


public class NameUnitTest {

    private String mFullName = "John Robert Edwards";

    @Test
    public void testGetFirstName() {

        String actualFirstName = "John";
        String resultFirstName = NameUtils.getFirstName(mFullName);

        assertEquals(actualFirstName, resultFirstName);
    }

    @Test
    public void testGetMiddleName() {

        String actualMiddleName = "Robert";
        String resultMiddleName = NameUtils.getMiddleName(mFullName);

        assertEquals(actualMiddleName, resultMiddleName);
    }

    @Test
    public void testGetLastName() {

        String actualLastName = "Edwards";
        String resultLastName = NameUtils.getLastName(mFullName);

        assertEquals(actualLastName, resultLastName);
    }

}