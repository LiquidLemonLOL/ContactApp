package se.lexicon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ContactApp {
    static void main() {

        //Sets relative path to read from, also creates directory if not present
        Path file = Path.of("data/contacts.csv");
        try {
            Files.createDirectories(Path.of("data"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //instantiates classes
        ContactCsvStorage csvStorage = new ContactCsvStorage(file);
        ContactData data = new ContactData();
        ContactValidation validation = new ContactValidation(data);
        ContactConsole console = new ContactConsole(validation);


        try {
            //Creates and populates HashMap from (if it exists) data stored in filepath
            data.loadFrom(csvStorage.load());
        } catch (Exception e) {
            IO.println("Failed to load contacts.");
        }

        console.start();

        try {
            //save possibly edited HashMap when user exits
            csvStorage.save(data.getMap());
        } catch (Exception e) {
            IO.println("Failed to save contacts.");
        }

    }
}
