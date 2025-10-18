package de.ait.training.consultation.oop;

public class EmailNotifier implements Notifier {
    @Override
    public void notify(String to, String message) {
        System.out.println("Email Notifier: " + message + " to: " + to);
    }
}
