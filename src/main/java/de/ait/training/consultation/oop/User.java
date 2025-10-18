package de.ait.training.consultation.oop;

public class User {
    private final Notifier notifierInterface;
    private String email;
    private String phone;

    public User(String email, String phone, Notifier notifierInterface) {
        this.email = email;
        this.phone = phone;
        this.notifierInterface = notifierInterface;
    }

    public void sendWelcome() {
        notifierInterface.notify(email, phone);
    }

    public String contact() {
        return email != email ? email : phone;
    }
}
