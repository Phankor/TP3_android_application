package com.example.tp3_android_marchal;

public class Account{
    // fields
    int id;
    String accountName;
    String amount;
    String iban;
    String currency;

    // constructors
    public Account() {}
    public Account(int id, String accountName, String amount, String iban, String currency) {
        this.id = id;
        this.accountName = accountName;
        this.amount = amount;
        this.iban = iban;
        this.currency = currency;
    }

    // properties
    public void setID2(int id) {
        this.id = id;
    }
    public int getID2() {
        return this.id;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public String getAccountName() {
        return this.accountName;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getAmount() {
        return this.amount;
    }
    public void setIban(String iban) {
        this.iban = iban;
    }
    public String getIban() {
        return this.iban;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getCurrency() {
        return this.currency;
    }

    // toString
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountName='" + accountName + '\'' +
                ", amount='" + amount + '\'' +
                ", iban='" + iban + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
