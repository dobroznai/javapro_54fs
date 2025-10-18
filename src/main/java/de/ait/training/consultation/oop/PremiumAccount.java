package de.ait.training.consultation.oop;

public class PremiumAccount extends BankAccountNew {

    public PremiumAccount(double balance) {
        super(balance);
    }

    @Override
    void updateBalance() {
        System.out.println("Premium Account balance is: " + balance);
    }

    @Override
    void printBalance() {
        System.out.println("Premium Account balance is: " + balance);

    }
}
