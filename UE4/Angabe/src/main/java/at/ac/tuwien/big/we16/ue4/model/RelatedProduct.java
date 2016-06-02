package at.ac.tuwien.big.we16.ue4.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class RelatedProduct extends AbstractPersistentObject {

    private String name_en;
    private String name_de;

    @ManyToOne
    private Product product;

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_de() {
        return name_de;
    }

    public void setName_de(String name_de) {
        this.name_de = name_de;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
