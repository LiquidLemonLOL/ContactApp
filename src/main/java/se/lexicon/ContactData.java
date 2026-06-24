package se.lexicon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.*;

public class ContactData {


        //HashMap of all contacts, seperated through UUID
        private final Map<UUID, Contact> contacts = new HashMap<>();

        public void save(Contact contact) {
            contacts.put(contact.getId(), contact);
        }

        //helper method to sort on last name then first name
        private Comparator<Contact> sortComparator() {
            return Comparator.comparing(Contact::getLastName, String.CASE_INSENSITIVE_ORDER).
                    thenComparing(Contact::getFirstName, String.CASE_INSENSITIVE_ORDER);
        }

        //get all contacts, sorted alphabetically in a list
        public List<Contact> getContacts() {
            return contacts.values().stream().sorted(sortComparator()).toList();
        }

        //Optional used to handle null contacts
        public Optional<Contact> getContactById (UUID id) {
            return Optional.ofNullable(contacts.get(id));
        }

        //Extendable search function to search based on Contact class fields
        //return contact if the repository has contact which corresponds with the search prompt/query, quite wordy
        //then returns with helper sort method and thrown into the returned list
        public List<Contact> search(String prompt){
            String lowerCasePrompt = prompt.toLowerCase();
            return contacts.values().stream()
                    .filter(contact ->
                        contact.getFirstName().toLowerCase().contains(lowerCasePrompt) ||
                        contact.getLastName().toLowerCase().contains(lowerCasePrompt) ||
                        contact.getPhoneNumber().toLowerCase().contains(lowerCasePrompt) ||
                        contact.getEmail().toLowerCase().contains(lowerCasePrompt) ||
                        contact.getAddress().toLowerCase().contains(lowerCasePrompt))
                    .sorted(sortComparator())
                    .toList();
        }

        public void remove(UUID uuid) {
            contacts.remove(uuid);
        }

        //check if UUID (contact) exists
        public boolean exists(UUID id) {
            return contacts.containsKey(id);
        }

}
