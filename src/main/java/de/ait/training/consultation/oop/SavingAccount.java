package de.ait.training.consultation.oop;

public class SavingAccount extends BankAccountNew {
    public SavingAccount(double balance) {
        super(balance);
    }

    @Override
    void updateBalance() {
        balance += balance * 0.1;
        System.out.println("Saving Account balance update is: " + balance);
    }

    // после обновленмя и реализии abstract method & class розрешен доступ
    void printBalance() {
        System.out.println("Saving Account balance is: " + balance);
    }

}
