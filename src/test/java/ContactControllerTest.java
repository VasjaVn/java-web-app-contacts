import com.contacts.common.message.Message;
import com.contacts.common.message.MessageSrcCode;
import com.contacts.common.util.MessageFactory;
import com.contacts.controller.ContactController;
import com.contacts.entity.Contact;
import com.contacts.service.ContactService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ContactControllerTest {
    private static final Long CLIENT_ID = 1L;

    private static final String EXPECTED_VIEW_NAME_SHOW = "contacts/show";
    private static final String EXPECTED_VIEW_NAME_DELETE = "redirect:/contacts/";

    private static final String ATTRIBUTE_NAME_CONTACT = ContactController.ATTRIBUTE_NAME_CONTACT;
    private static final String ATTRIBUTE_NAME_MESSAGE = ContactController.ATTRIBUTE_NAME_MESSAGE;

    private ContactController controller;
    private ContactService contactService;
    private MessageFactory messageFactory;

    private Contact contact = new Contact();
    private Message message = new Message();

    @Before
    public void beforeTest() {
        prepareMockContactService();
        prepareMockMessageFactory();

        controller = new ContactController();
        controller.setContactService(contactService);
        controller.setMessageFactory(messageFactory);
    }

    @Test
    public void testShowContact() {
        ModelMap modelMap = new ModelMap();
        String viewName = controller.show(CLIENT_ID, modelMap);

        assertEquals(EXPECTED_VIEW_NAME_SHOW, viewName );
        assertSame(contact, modelMap.get(ATTRIBUTE_NAME_CONTACT));
        verify(contactService).findById(CLIENT_ID);
    }

    @Test
    public void testDeleteContact() {
        RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        Locale locale = Locale.ENGLISH;

        String viewName = controller.delete(CLIENT_ID, redirectAttributes, locale);

        Message actualMessage = (Message) redirectAttributes.getFlashAttributes().get(ATTRIBUTE_NAME_MESSAGE);

        assertEquals(EXPECTED_VIEW_NAME_DELETE, viewName );
        assertSame(message, actualMessage);
    }

    private void prepareMockContactService() {
        contactService = mock(ContactService.class);
        when(contactService.findById(CLIENT_ID)).thenReturn(contact);
    }

    private void prepareMockMessageFactory() {
        messageFactory = mock(MessageFactory.class);
        when(messageFactory.getMessage(any(MessageSrcCode.class), any(Object[].class), any(Locale.class))).thenReturn(message);
    }
}
