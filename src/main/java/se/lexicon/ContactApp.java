package se.lexicon;


public class ContactApp {
    static void main() {

        ContactData data = new ContactData();
        ContactValidation validation = new ContactValidation(data);
        ContactConsole console = new ContactConsole(validation);

        console.start();

    }
}
