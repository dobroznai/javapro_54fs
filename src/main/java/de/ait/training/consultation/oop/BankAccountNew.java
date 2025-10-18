package de.ait.training.consultation.oop;

abstract class BankAccountNew {
    protected double balance;

    protected BankAccountNew(double balance) {
        this.balance = balance;
    }

    public void changeBalance(double balance) {
        this.balance = balance;
    }

    //полиморфизм
    abstract void updateBalance();

    abstract void printBalance();
}
