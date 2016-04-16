package at.ac.tuwien.big.we16.ue2.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Auction {
    private long highestBid;
    private String expirationDate;
    private User highestBidder;
    private String name;
    private String image;

    public String displayHighestBid(){
        return ((highestBid/100) + "," + (highestBid%100==0?"":highestBid%100) + "€");
    }

    public long getHighestBid(){
        return highestBid;
    }

    public void setHighestBid(long highestBid){
        this.highestBid = highestBid;
    }

    /*
    @return The expiration date, format: "yyyy,MM,dd,mm,ss,sss"
     */
    public String getExpirationDate(){
        return expirationDate;
    }

    /*
    @param expirationDate format: "yyyy,MM,dd,mm,ss,sss"
     */
    public void setExpirationDate(String expirationDate){
        this.expirationDate = expirationDate;
    }

    public boolean isExpired(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd,mm,ss,sss");
        boolean expired = false;
        try {
            Date expDate = sdf.parse(expirationDate);
            expired = expDate.before(new Date());
        } catch (ParseException e) {
            System.out.println("Parsing of Date-String failed");
            e.printStackTrace();
        }
        return expired;
    }

    public User getHighestBidder(){
        return highestBidder;
    }

    public void setHighestBidder(User highestBidder){
        this.highestBidder = highestBidder;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }
}
