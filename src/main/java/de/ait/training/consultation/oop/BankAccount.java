package de.ait.training.consultation.oop;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public class BankAccount {


    private double balance = 50;

    public double getBalance() {
        log.info("Balance {}", balance);
        return balance;
    }

    public void withdrawal(double amount) {
        if (amount < 0 || amount > balance) {
            if (amount < 0) {
                log.warn("Invalid amount. {} < 0", amount);
            }
            if (amount > balance) {
                log.warn("Invalid amount. Amount: {} > Balance: {}", amount, balance);
                System.out.println("Invalid amount");
                return;
            }
        }
        log.info("Withdrawal {}", amount);
        if (amount > 0) {
            balance -= amount;
        }
    }
    
}
