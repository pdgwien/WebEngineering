package at.ac.tuwien.big.we16.ue3.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    private String salutation;
    @Basic
    private String firstname;
    @Basic
    private String lastname;
    @Basic
    private String email;
    @Basic
    private String password;
    @Basic
    private Date date;
    @Basic
    private int balance;
    @Basic
    private int runningAuctionsCount;
    @Basic
    private int wonAuctionsCount;
    @Basic
    private int lostAuctionsCount;

    public int getId() {
        return id;
    }


    public String getFullName() {
        return this.firstname + " " + this.lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getConvertedBalance() {
        float convertedBalance = (float) this.balance;
        return convertedBalance / 100;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void decreaseBalance(int amount) {
        this.balance -= amount;
    }

    public void increaseBalance(int amount) {
        this.balance += amount;
    }

    public int getRunningAuctionsCount() {
        return this.runningAuctionsCount;
    }

    public void incrementRunningAuctions() {
        this.runningAuctionsCount++;
    }

    public void decrementRunningAuctions() {
        this.runningAuctionsCount--;
    }

    public int getWonAuctionsCount() {
        return this.wonAuctionsCount;
    }

    public int getLostAuctionsCount() {
        return this.lostAuctionsCount;
    }

    public void incrementLostAuctionsCount() {
        this.lostAuctionsCount++;
    }

    public void incrementWonAuctionsCount() {
        this.wonAuctionsCount++;
    }

    public boolean hasSufficientBalance(int amount) {
        return this.balance >= amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

}
