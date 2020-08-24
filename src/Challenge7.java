import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Challenge7 {
    public static void main(String[] args) {
        BankAccount account1 = new BankAccount("12345-678", 500.00);
        BankAccount account2 = new BankAccount("98765-432", 1000.00);

        new Thread(new Transfer(account1, account2, 10.00), "Transfer1").start();
        new Thread(new Transfer(account2, account1, 55.88), "Transfer2").start();
    }
}

class BankAccount {
    private double balance;
    private String accountNumber;
    private Lock lock;

    BankAccount(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.lock = new ReentrantLock();
    }

    public boolean withdraw(double amount) {
        try{
            if (lock.tryLock()) {
                try {
                    // Simulate database access
                    balance -= amount;
                    Thread.sleep(100);
                }finally {
                    lock.unlock();
                }
                System.out.printf("%s: Withdrew %f\n", Thread.currentThread().getName(), amount);
                return true;
            } else {
                System.out.println("lock not found");
                return false;
            }
        }catch ( InterruptedException e){
            System.out.println("Interrupted Exception");
        }
        return false;
    }

    public boolean deposit(double amount) {
        try{
            if (lock.tryLock()) {
                try {
                    // Simulate database access
                    balance += amount;
                    Thread.sleep(100);
                }finally {
                    lock.unlock();
                }
                System.out.printf("%s: Deposited %f\n", Thread.currentThread().getName(), amount);
                return true;
            } else {
                System.out.println("lock not found");
            }
        } catch (InterruptedException e) {
        }
        return false;
    }

    public boolean transfer(BankAccount destinationAccount, double amount) {
        if (withdraw(amount)) {
            if (destinationAccount.deposit(amount)) {
                return true;
            }
            else {
                // The deposit failed. Refund the money back into the account.
                System.out.printf("%s: Destination account busy. Refunding money\n",
                        Thread.currentThread().getName());
                deposit(amount);
            }
        }

        return false;
    }
}

class Transfer implements Runnable {
    private BankAccount sourceAccount, destinationAccount;
    private double amount;

    Transfer(BankAccount sourceAccount, BankAccount destinationAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }

    public void run() {
        while (!sourceAccount.transfer(destinationAccount, amount))
            continue;
        System.out.printf("%s completed\n", Thread.currentThread().getName());
    }

}