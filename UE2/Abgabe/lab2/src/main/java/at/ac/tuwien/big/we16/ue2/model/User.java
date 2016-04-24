package at.ac.tuwien.big.we16.ue2.model;

import java.text.DecimalFormat;

public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private long balance;
    private int currentAuctions;
    private int lostAuctions;
    private int wonAuctions;


    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String displayBalance() {
        DecimalFormat decim = new DecimalFormat("0.00");
        return decim.format(balance/100.0);
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public int getCurrentAuctions() {
        return currentAuctions;
    }

    public void setCurrentAuctions(int currentAuctions) {
        this.currentAuctions = currentAuctions;
    }

    public int getLostAuctions() {
        return lostAuctions;
    }

    public void setLostAuctions(int lostAuctions) {
        this.lostAuctions = lostAuctions;
    }

    public int getWonAuctions() {
        return wonAuctions;
    }

    public void setWonAuctions(int wonAuctions) {
        this.wonAuctions = wonAuctions;
    }

}
