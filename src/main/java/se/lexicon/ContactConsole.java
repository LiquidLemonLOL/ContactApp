package se.lexicon;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ContactConsole {

        private final ContactValidation validation;


        //uses validation/logic class to function
        public ContactConsole(ContactValidation validation) {
            this.validation = validation;
        }

        public void start() {
            IO.println("=================================");
            IO.println("   LEXICON CONTACT MANAGER APP   ");
            IO.println("=================================");

            boolean active = true;

            while (active) {
                printMenu();
                String userChoice = IO.readln("Choose an option: ");

                switch (userChoice) {
                    case "1" -> createContactMenu();
                    case "2" -> displayAllContactsMenu();
                    case "3" -> searchContactMenu();
                    case "4" -> updateContactMenu();
                    case "5" -> deleteContactMenu();
                    case "0" ->  {
                        IO.println("Goodbye!");
                        active = false;
                    }
                    default -> IO.println("Invalid option, try again.");
                }
            }
        }


        //Helper methods

        private void printMenu() {
            IO.println("---------------------------------");
            IO.println("1. Create a new contact");
            IO.println("2. Display all contacts (in alphabetical order)");
            IO.println("3. Search contacts");
            IO.println("4. Update contact");
            IO.println("5. Delete contact");
            IO.println("0. Exit");
            IO.println("---------------------------------");
        }

        //Prompt user for all contact data inside a try-catch
        private void createContactMenu() {
            IO.println("===      Add new contact      ===");
            try {
                String firstName = IO.readln("Enter first name: ");
                String lastName = IO.readln("Enter last name: ");
                String email = IO.readln("Enter email: ");
                String phone = IO.readln("Enter phone number: ");
                String address = IO.readln("Enter address: ");

                Contact contact = validation.addContact(firstName, lastName, email, phone, address);
                IO.println("Contact added: " + contact.getFullName() + " (ID: " + contact.getId() + ")");
            } catch (IllegalArgumentException e) {
                IO.println("Error: " + e.getMessage());
            }
        }

        private void displayAllContactsMenu() {
            List<Contact> contacts = validation.getAllContacts();
            if (contacts.isEmpty()) {
                IO.println("There are no contacts in the system.");
                return;
            }
            IO.println("===       All contacts       ===");
            contacts.forEach(contact -> IO.println(contact.toString()));
        }


        //prompt user to search with any contact field
        private void searchContactMenu() {
            IO.println("===      Search contacts     ===");
            String prompt = IO.readln("Search contact (Type in a first/last name, phone number, email or address): ");
            try {
                List<Contact> res = validation.searchContacts(prompt);
                if (res.isEmpty()) {
                    IO.println("There are no matches with \"" + prompt + "\" in the system.");
                }
                else {
                    IO.println(res.size() + " contacts found:");
                    res.forEach(contact -> IO.println(contact.toString()));
                }
            } catch (IllegalArgumentException e) {
                IO.println("Error: " + e.getMessage());
            }
        }

        private void updateContactMenu() {
            IO.println("===      Update contact     ===");
            UUID id = promptUUID();
            if (id == null) { return; }

            Optional<Contact> contactExists = validation.getContact(id);
            if (contactExists.isEmpty()) {
                IO.println("There is no contact with ID: " + id);
                return;
            }

            Contact contact = contactExists.get();
            IO.println("Editing " + contact.getFullName() + " with ID: " + contact.getId());
            IO.println("To keep current values for a field, press Enter.");

            try {
                String firstName = userInputOrDefault("First name", contact.getFirstName());
                String lastName = userInputOrDefault("Last name", contact.getLastName());
                String email = userInputOrDefault("Email", contact.getEmail());
                String phoneNumber = userInputOrDefault("Phone number", contact.getPhoneNumber());
                String address = userInputOrDefault("Address", contact.getAddress());

                validation.updateContact(id, firstName, lastName, email, phoneNumber, address);
                IO.println("Updated " + contact.getFullName() + "with ID : " + contact.getId());
            } catch (IllegalArgumentException e) {
                IO.println("Error: " + e.getMessage());
            }
        }

        private void deleteContactMenu() {
            IO.println("===      Delete contact     ===");
            UUID id = promptUUID();
            if (id == null) { return; }

            validation.getContact(id).ifPresent(
                    contact -> {
                        String userConfirm = IO.readln("Are you sure you want to delete "
                                + contact.getFullName() + " with ID " + id + " from the system? (yes/no) ");
                        if (userConfirm.equalsIgnoreCase("yes")) {
                            validation.deleteContact(id);
                            IO.println("Deleted " + contact.getFullName() + " with ID: " + id + ".");
                        } else {
                            IO.println("Deletion canceled.");
                        }
                    });
        }

        //helper UUID method
        private UUID promptUUID () {
            String input = IO.readln("Enter contact ID: ");
            try {
                return UUID.fromString(input.trim());
            } catch (IllegalArgumentException e) {
                IO.println("Invalid ID format");
                return null;
            }
        }

        private String userInputOrDefault(String prompt, String defaultValue) {
            IO.println(prompt + ": " + "[Current: " + defaultValue + "]");
            String input = IO.readln("Update " + prompt + ":");
            //if user only clicks enter, take defaultValue ie keep current
            return input.isBlank() ? defaultValue : input;
        }


}
