package at.ac.tuwien.big.we16.ue3.model;

import javax.persistence.*;

@Entity
public class RelatedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    private String nameDe;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return nameDe;
    }

    public void setName(String nameDe) {
        this.nameDe = nameDe;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
