package at.ac.tuwien.big.we16.ue2.model;

import java.util.ArrayList;
import java.util.List;

public class AuctionStorage {

    private List<Auction> auctions;

    public AuctionStorage(){
        auctions = new ArrayList<Auction>();
    }

    public List<Auction> getAuctions(){
        List<Auction> l = new ArrayList<Auction>();
        l.addAll(auctions);
        return l;
    }

    public void addAuction(Auction auction){
        auctions.add(auction);
    }

    public Auction getAuctionByName(String name){
        for(Auction a : auctions){
            if(a.getName().equals(name)){
                return a;
            }
        }
        return null;
    }

    public List<Auction> getExpiredAuctions(){
        List<Auction> l = new ArrayList<Auction>();
        for(Auction a : auctions){
            if(a.isExpired()){
                l.add(a);
            }
        }
        return l;
    }
}
