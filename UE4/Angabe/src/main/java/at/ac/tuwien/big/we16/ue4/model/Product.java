package at.ac.tuwien.big.we16.ue4.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import at.ac.tuwien.big.we16.ue4.exception.InvalidBidException;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Product extends AbstractPersistentObject {
    private String name_en;
    private String name_de;
    private String image;
    private String imageAlt;
    private Date auctionEnd;
    private ProductType type;
    private int year;
    private String producer;
    private boolean expired;

    @OneToMany(fetch = FetchType.EAGER)
    private List<RelatedProduct> relatedProducts;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Bid> bids;

    public Product(String name_en, String image, String imageAlt, Date auctionEnd, ProductType type, int year, String producer, List<Bid> bids) {
        this.name_en = name_en;
        this.image = image;
        this.imageAlt = imageAlt;
        this.auctionEnd = auctionEnd;
        this.type = type;
        this.year = year;
        this.producer = producer;
        this.expired = false;
        this.bids = bids;
    }

    public Product() {}

    public void setAllValues(Product product) {
        this.name_en = product.name_en;
        this.name_de = product.name_de;
        this.image = product.image;
        this.imageAlt = product.imageAlt;
        this.auctionEnd = product.auctionEnd;
        this.type = product.type;
        this.year = product.year;
        this.producer = product.producer;
        this.expired = product.expired;
        this.relatedProducts = product.relatedProducts;
        this.bids = product.bids;
    }

    public Bid getHighestBid() {
        Bid highest = null;
        int highestAmount = 0;
        for (Bid bid : this.bids) {
            if (bid.getAmount() > highestAmount) {
                highest = bid;
            }
        }
        return highest;
    }

    public boolean hasAuctionEnded() {
        return this.getAuctionEnd().before(new Date());
    }

    public void addBid(Bid bid) throws InvalidBidException {
        this.bids.add(bid);
    }

    public boolean hasExpired() {
        return expired;
    }

    public void setExpired() {
        this.expired = true;
    }

    public Set<User> getUsers() {
        Set<User> users = new HashSet<>();
        for (Bid bid : this.bids) {
            users.add(bid.getUser());
        }
        return users;
    }

    public boolean hasBids() {
        return this.bids.size() > 0;
    }

    public boolean isValidBidAmount(int amount) {
        return !this.hasBids() || this.getHighestBid().getAmount() < amount;
    }

    public boolean hasBidByUser(User user) {
        for (Bid bid : this.bids) {
            if (bid.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

    //getter and setter

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageAlt() {
        return imageAlt;
    }

    public void setImageAlt(String imageAlt) {
        this.imageAlt = imageAlt;
    }

    public Date getAuctionEnd() {
        return auctionEnd;
    }

    public void setAuctionEnd(Date auctionEnd) {
        this.auctionEnd = auctionEnd;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public List<RelatedProduct> getRelatedProducts() {
        return relatedProducts;
    }

    public void setRelatedProducts(List<RelatedProduct> similarProducts) {
        this.relatedProducts = similarProducts;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }
}
