package model;

public class User {
    private final String name;
    private final String email;
    private final String contact;

    public User(String name, String email, String contact) {
        this.name = name;
        this.email = email;
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }
}
