package at.ac.tuwien.big.we16.ue4.model;

import com.sun.istack.internal.NotNull;

import at.ac.tuwien.big.we16.ue4.exception.InvalidBidException;

import org.hibernate.validator.constraints.Email;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class User extends AbstractPersistentObject {

    @NotNull
    private String salutation;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    @Column(unique = true)
    @Email
    private String email;
    @Size(min = 4, max = 12)
    private String password;
    @Temporal(TemporalType.DATE)
    private Date date;

    private int balance;
    private int runningAuctionsCount;
    private int wonAuctionsCount;
    private int lostAuctionsCount;

    public User() {}

    public User(String salutation, String firstname, String lastname, String email, String password, Date date) {
        this.salutation = salutation;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.date = date;
        this.balance = 150000;
        this.runningAuctionsCount = 0;
        this.wonAuctionsCount = 0;
        this.lostAuctionsCount = 0;
    }

    public String getFullName() {
        return this.firstname + " " + this.lastname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public float getConvertedBalance() {
        float convertedBalance = (float)this.balance;
        return convertedBalance / 100;
    }

    public void decreaseBalance(int amount) throws InvalidBidException {
        this.balance -= amount;
    }

    public void increaseBalance(int amount) throws InvalidBidException {
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
