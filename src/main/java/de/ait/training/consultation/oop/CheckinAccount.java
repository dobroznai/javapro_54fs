package de.ait.training.consultation.oop;

public class CheckinAccount extends BankAccountNew {
    public CheckinAccount(double balance) {
        super(balance);
    }

    @Override
    void updateBalance() {
        balance -= balance * 0.1;
        System.out.println("Checkin Account balance is: " + balance);
    }

    void printBalance() {
        System.out.println("Saving Account balance is: " + balance);
    }
}
