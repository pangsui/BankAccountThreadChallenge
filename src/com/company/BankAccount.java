package com.company;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount{
        private double balance;
        private String accountNumber;
        private Lock reentrantLock;

        public BankAccount(String accountNumber, double initialBalance) {
            this.accountNumber = accountNumber;
            this.balance = initialBalance;
            this.reentrantLock = new ReentrantLock();
        }

//        public synchronized void deposit(double amount) {
//            balance += amount;
//        }
//
//        public synchronized void withdraw(double amount) {
//            balance -= amount;
//        }
//
//    public double getBalance() {
//        return balance;
//    }
public void deposit(double amount) {
            boolean status = false;
            try{
                if (reentrantLock.tryLock(1000, TimeUnit.MILLISECONDS)){
                    try{
                        balance += amount;
                        status = true;
                    }finally {
                        reentrantLock.unlock();
                    }
                } else {
                    System.out.println("Could not get the lock");
                }
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
    System.out.println("Transaction status :"+status);

}
    public void withdraw(double amount){
            boolean status = false;
        try{
            if (reentrantLock.tryLock(1000, TimeUnit.MILLISECONDS)){
                try{
                    balance -= amount;
                    status = true;
                }finally {
                    reentrantLock.unlock();
                }
            } else {
                System.out.println("Could not get the lock");
            }
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        System.out.println("Transaction status :" +status);
    }

    public double getBalance() {
                return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void printAccountNumber(){
        System.out.println("account :"+ accountNumber);
    }
}

