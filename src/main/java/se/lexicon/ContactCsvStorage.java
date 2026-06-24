package se.lexicon;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class ContactCsvStorage {
    //filepath to read from
    private final Path file;

    public ContactCsvStorage(Path file) {
        this.file = file;
    }

    //OpenCSV implementation, writes all entries in HashMap, used when exiting app
    public void save(Map<UUID, Contact> contacts) throws IOException {
        //
        try (CSVWriter writer =
                     new CSVWriter(Files.newBufferedWriter(file))) {
            //Writes the fields
            writer.writeNext(new String[]{
                    "id",
                    "firstName",
                    "lastName",
                    "email",
                    "phoneNumber",
                    "address"
            });
            //Writes each contact info
            for (Contact contact : contacts.values()) {
                writer.writeNext(new String[]{
                        contact.getId().toString(),
                        contact.getFirstName(),
                        contact.getLastName(),
                        contact.getEmail(),
                        contact.getPhoneNumber(),
                        contact.getAddress()
                });
            }
        }
    }

    //OpenCSV implementation, populates HashMap based on if persistent data exists
    public Map<UUID, Contact> load() throws IOException {
        Map<UUID, Contact> contacts = new HashMap<>();

        //returns empty hashmap
        if (!Files.exists(file)) {
            return contacts;
        }

        try (CSVReader reader = new CSVReader(Files.newBufferedReader(file))) {
            //Reads entire file into a list with each element being a String[]
            List<String[]> rows = reader.readAll();

            //Read all but header (the standard fields), goes through each field, extendable if needed
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                UUID id = UUID.fromString(row[0]);
                Contact contact = new Contact(
                        id,
                        row[1],
                        row[2],
                        row[3],
                        row[4],
                        row[5]
                );
                //add entry into hashmap
                contacts.put(id, contact);
            }
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
        return contacts;
    }

}
