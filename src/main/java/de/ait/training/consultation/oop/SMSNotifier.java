package de.ait.training.consultation.oop;

public class SMSNotifier implements Notifier {

    @Override
    public void notify(String to, String message) {
        System.out.println("SMS Notifier: " + message + " to: " + to);
    }
}
