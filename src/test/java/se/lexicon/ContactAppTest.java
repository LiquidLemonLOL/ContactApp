package se.lexicon;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class ContactAppTest {

    private ContactData data;
    private ContactValidation validation;

    @BeforeEach
    public void setup() {
        data = new ContactData();
        validation = new ContactValidation(data);
    }

    @Test
    @DisplayName("Successful contact add")
    void testSuccessfulAdd() {
        //adds a valid contact
        Contact contact = validation.addContact("John", "Doe", "good@email.com", "0705394817", "Street 1");
        //Verifies that the contact is stored in the HashMap, importantly through its UUID
        assertTrue(data.getContactById(contact.getId()).isPresent());
    }

    @Test
    @DisplayName("Unsuccessful add - bad email")
    void testUnsuccessfulAddEmail() {
        String badEmail = "verybademailatbogus.wabbajack";
        IllegalArgumentException e =
        assertThrows(IllegalArgumentException.class, () ->
                validation.addContact("John", "Doe", badEmail, "0705394817", "Street 1"));
        assertEquals(("Email address " + badEmail + " is not valid."), e.getMessage());
    }

    @Test
    @DisplayName("Unsuccessful add - bad phone number")
    void testUnsuccessfulAddPhoneNumber() {
        String badNumber = "zero837sevendialdot2817";
        //Add an invalid contact
        IllegalArgumentException e =
                assertThrows(IllegalArgumentException.class, () -> validation.addContact("John", "Doe", "good@email.com", badNumber, "Street 1"));
        assertEquals(("Phone number " + badNumber + " is not valid."), e.getMessage());
    }

    @Test
    @DisplayName("Unsuccessful add - null name")
    void testUnsuccessfulAddName() {
        //Add an invalid contact
        assertThrows(IllegalArgumentException.class, () -> validation.addContact(null, "Doe", "good@email.com", "0705394817", "Street 1"));
    }

    @Test
    @DisplayName("Unsuccessful add - null address")
    void testUnsuccessfulAddaAddress() {
        //Add an invalid contact
        assertThrows(IllegalArgumentException.class, () -> validation.addContact("John", "Doe", "good@email.com", "0705394817", null));
    }

    @Test
    @DisplayName("Successful removal")
    void testSuccessfulRemove() {
        Contact contact = validation.addContact("John", "Doe", "good@email.com", "0705394817", "Street 1");
        assertTrue(data.getContactById(contact.getId()).isPresent());
        validation.deleteContact(contact.getId());
        assertTrue(data.getContacts().isEmpty());
    }

    @Test
    @DisplayName("Unsuccessful removal")
    void testUnsuccessfulRemove() {
        Contact contact = validation.addContact("John", "Doe", "good@email.com", "0705394817", "Street 1");
        UUID badUUID = UUID.randomUUID();
        IllegalArgumentException e =
                assertThrows(IllegalArgumentException.class, () -> validation.deleteContact(badUUID));
        assertEquals(("No contact with id " + badUUID + " exists."), e.getMessage());
        //Make sure that no removal operation has happened
        assertTrue(data.getContactById(contact.getId()).isPresent());
    }

    @Test
    @DisplayName("Successful update")
    void testSuccessfulUpdate() {
        Contact contact = validation.addContact("John", "Doe", "good@email.com", "0705394817", "Street 1");
        UUID id = contact.getId();
        validation.updateContact(id, "Johnny", "Does", "verygood@email.com", "0705394828", "Street 2");
        Contact updatedContact = data.getContactById(id).orElseThrow();
        assertAll(
                () -> assertEquals("Johnny", updatedContact.getFirstName()),
                () -> assertEquals("Does", updatedContact.getLastName()),
                () -> assertEquals("verygood@email.com", updatedContact.getEmail()),
                () -> assertEquals("0705394828", updatedContact.getPhoneNumber()),
                () -> assertEquals("Street 2", updatedContact.getAddress())
        );
    }

    @Test
    @DisplayName("Unsuccessful update, bad email")
    void testUnsuccessfulUpdate() {
        Contact contact = validation.addContact("John", "Doe", "good@email.com", "0705394817", "Street 1");
        UUID id = contact.getId();
        String badEmail = "verybademailatbogus.wabbajack";
        IllegalArgumentException e =
                assertThrows(IllegalArgumentException.class, () -> validation.updateContact(id, "Johnny", "Does", badEmail, "0705394828", "Street 2"));
        assertEquals(("Email address " + badEmail + " is not valid."), e.getMessage());
    }

    @Test
    @DisplayName("Successful search")
    void testSuccessfulSearch() {
        Contact contact = validation.addContact("John", "Doe", "good@email.com", "0705394817", "Street 1");
        String searchPrompt = contact.getFirstName();
        List<Contact> searchRes = validation.searchContacts(searchPrompt);
        //Assert that search has 1 result
        assertEquals(1, searchRes.size());
        //Assert that the only result is the same as object that was searched for
        assertSame(contact, searchRes.getFirst());
    }

    @Test
    @DisplayName("Unsuccessful search")
    void testUnsuccessfulSearch() {
        Contact contact = validation.addContact("John", "Doe", "good@email.com", "0705394817", "Street 1");
        assertTrue(data.getContactById(contact.getId()).isPresent());
        String searchPrompt = "Jermaine";
        List<Contact> searchRes = validation.searchContacts(searchPrompt);
        //Assert that search has 0 results
        assertEquals(0, searchRes.size());
    }

    @Test
    @DisplayName("Unsuccessful search - bad prompt")
    void testUnsuccessfulSearchBadPrompt() {
        Contact contact = validation.addContact("John", "Doe", "good@email.com", "0705394817", "Street 1");
        assertTrue(data.getContactById(contact.getId()).isPresent());
        IllegalArgumentException e =
                assertThrows(IllegalArgumentException.class, () -> validation.searchContacts(null));
        assertEquals(("Search prompt cannot be empty or null."), e.getMessage());
    }




}
