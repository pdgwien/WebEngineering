package at.ac.tuwien.big.we16.ue3.model;

import javax.persistence.*;

@Entity
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    private int amount;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Bid() {
    }

    public Bid(int centAmount, User user) {
        amount = centAmount;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public float getConvertedAmount() {
        float convertedAmount = (float)this.amount;
        return convertedAmount / 100;
    }

    public User getUser() {
        return user;
    }

    public boolean isBy(User user) {
        return this.user.equals(user);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
