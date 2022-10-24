package example;

import java.io.Serializable;

public class User implements Serializable {
    private double balance;
    private String currency = "";

    public User(double balance, String currency) {
        this.balance = balance;
        this.currency = currency;
    }

    public User(){

    }

    public double getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }
}
