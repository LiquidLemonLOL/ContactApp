package se.lexicon;

import java.util.*;
import java.util.regex.Pattern;

public class ContactValidation {


    //searched up, working regex
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    //same here
    public static final Pattern VALID_PHONE_NUMBER_REGEX =
            Pattern.compile("^(\\+\\d{1,2}\\s?)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$", Pattern.CASE_INSENSITIVE);

    //keep contact data, as the class does Contact logic and validates fields
    private final ContactData data;

    public ContactValidation(ContactData data) {
        this.data = data;
    }

    //CRUD functionality

    //Create contact validation
    public Contact addContact(String firstName, String lastName, String email, String phoneNumber, String address) {
        validateName(firstName, lastName);
        validateEmail(email);
        validatePhone(phoneNumber);
        validateAddress(address);

        Contact contact = new Contact(firstName, lastName, email, phoneNumber, address);
        data.save(contact);
        return contact;
    }


    //Read contact validation, handling null values etc

    public Optional<Contact> getContact(UUID id) {
        return data.getContactById(id);
    }

    public List<Contact> getAllContacts() {
        return data.getContacts();
    }

    public List<Contact> searchContacts(String prompt) throws IllegalArgumentException {
        if (prompt == null || prompt.isEmpty()) {
            throw new IllegalArgumentException("Search prompt cannot be empty or null.");
        }
        return data.search(prompt);
    }

    //Update contact validation

    //Same checks as add but needs to find existing Contact through UUID + new sets if checks go through
    public void updateContact(UUID id, String firstName, String lastName, String email, String phoneNumber, String address) {
        Contact contact = data.getContactById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contact with id " + id + " does not exist."));
        validateName(firstName, lastName);
        validateEmail(email);
        validatePhone(phoneNumber);
        validateAddress(address);

        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setEmail(email);
        contact.setPhoneNumber(phoneNumber);
        contact.setAddress(address);

        data.save(contact);
    }


    //Delete validation

    public void deleteContact(UUID id) {
        if (!data.exists(id)){
            throw new IllegalArgumentException("No contact with id " + id + " exists.");
        }
        data.remove(id);
    }

    //Validation for Contact data fields, helper methods

    //Validates both first and last name
    private void validateName(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or empty.");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or empty.");
        }
    }

    //Match phonenumber against regex
    private void validatePhone(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isBlank() && !VALID_PHONE_NUMBER_REGEX.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException("Phone number " + phoneNumber + " is not valid.");
        }
    }

    //match email against regex
    private void validateEmail(String email) {
        if (email != null && !email.isBlank() && !VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches()) {
            throw new IllegalArgumentException("Email address " + email + " is not valid.");
        }
    }


    //Simple null/blank check for address, no crazy checks required
    private void validateAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }
    }









}
