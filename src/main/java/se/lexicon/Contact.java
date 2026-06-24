package se.lexicon;

import java.util.UUID;

public class Contact {

    //extendable information, could add extra phone numbers etc
    private final UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;

    //new contact, UUID is generated
    public Contact(String firstName, String lastName, String email, String phoneNumber, String address) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    //load contacts, UUID is already present, don't generate UUID
    public Contact(UUID id, String firstName, String lastName, String email, String phoneNumber, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getFirstName() {return firstName;}
    public String getLastName() { return lastName;}
    public String getFullName() { return firstName + " " + lastName;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}
    public UUID getId() {return id;}
    public String getAddress() {return address;}

    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setAddress(String address) {this.address = address;}

    @Override
    public String toString() {
        return String.format("%s | Name: %s | Phone number: %s | Email: %s | Address: %s", id.toString(), getFullName(), getPhoneNumber(), getEmail(), getAddress());
    }
}
