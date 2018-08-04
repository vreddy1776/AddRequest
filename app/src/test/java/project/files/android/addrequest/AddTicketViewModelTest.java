package project.files.android.addrequest;

import org.junit.Test;

import project.files.android.addrequest.MVVM.AddTicket.AddTicketViewModel;
import project.files.android.addrequest.Utils.GlobalConstants;

public class AddTicketViewModelTest {


    /*
    @Mock
    private NotesRepository mNotesRepository;

    @Mock
    private ImageFile mImageFile;

    @Mock
    private AddNoteContract.View mAddNoteView;
    */

    private AddTicketViewModel mAddTicketViewModel;

    /*
    @Before
    public void setupAddNotePresenter() {

        MockitoAnnotations.initMocks(this);

        Context context = mock(Context.class);


        // Get a reference to the class under test
        mAddTicketViewModel = new AddTicketViewModel(new MockContext(), 5);
    }
    */

    @Test
    public void saveNoteToRepository_showsSuccessMessageUi() {

        mAddTicketViewModel = new AddTicketViewModel();


        mAddTicketViewModel.addTicket(
                        104601350,
                "Test Ticket Title",
                "test ticket description",
                "08-17-2016",
                "no_video",
                "no_video_local_uri",
                "no_video_internet_url",
                "234f783fdsb2248ahH2",
                "Bob Smith",
                "http://testurl.com",
                GlobalConstants.ADD_TICKET_TYPE);


        //Mockito.verify(mAddTicketViewModel.getTicket()).set("dummy text");


        /*
        verify(mNotesRepository).saveNote(any(Note.class)); // saved to the model
        verify(mAddNoteView).showNotesList(); // shown in the UI
        */
    }


}
