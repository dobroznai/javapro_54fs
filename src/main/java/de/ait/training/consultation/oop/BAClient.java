package de.ait.training.consultation.oop;

public class BAClient {

    static Notifier notifierInterface;

    public static void main(String[] args) {
//        BankAccount bankAccount = new BankAccount();
//        System.out.println(bankAccount.balance);
//        bankAccount.balance = 1000;
//        System.out.println(bankAccount.getBalance());
//        bankAccount.withdrawal(40);
//        System.out.println(bankAccount.getBalance());

//        Car car = new Car("Tesla");
//        car.move();

        BankAccountNew bankAccountOne = new SavingAccount(1000);
        BankAccountNew bankAccountTwo = new CheckinAccount(1000);

        bankAccountOne.updateBalance();
        bankAccountTwo.updateBalance();

        bankAccountOne.printBalance();

        //интерфейс - любая реализация
        User user = new User("test@test.com", "237927397", notifierInterface);
        user.sendWelcome();
        user.contact();

    }
}
