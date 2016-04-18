package at.ac.tuwien.big.we16.ue2.model;

import at.ac.tuwien.big.we16.ue2.productdata.JSONDataLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AuctionStorageFactory {

    static final long MINUTE_IN_MILLIS = 60000;

    private static AuctionStorage storage;

    private static void createStorage(){
        storage = new AuctionStorage();

        JSONDataLoader.Music[] music = JSONDataLoader.getMusic();
        for(JSONDataLoader.Music m : music){
            Auction a = initAuction(new Auction());
            a.setImage(m.getImg());
            a.setName(m.getAlbum_name());
            storage.addAuction(a);
        }

        JSONDataLoader.Book[] books = JSONDataLoader.getBooks();
        for(JSONDataLoader.Book b : books){
            Auction a = initAuction(new Auction());
            a.setImage(b.getImg());
            a.setName(b.getTitle());
            storage.addAuction(a);
        }

        JSONDataLoader.Movie[] movies = JSONDataLoader.getFilms();
        for(JSONDataLoader.Movie m : movies){
            Auction a = initAuction(new Auction());
            a.setImage(m.getImg());
            a.setName(m.getTitle());
            storage.addAuction(a);
        }
    }

    private static Auction initAuction(Auction a){
//        a.setExpirationDate("2016,04,17,15,30,00,000");
        a.setExpirationDate(buildExpirationDate());
        a.setHighestBid(0);
        a.setHighestBidder(null);
        return a;
    }

    public static AuctionStorage getAuctionStorage(){
        if (storage == null){
            createStorage();
        }
        return storage;
    }

    public static String buildExpirationDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss,sss");
        Calendar cal = Calendar.getInstance();
        Date date = new Date(cal.getTimeInMillis()+MINUTE_IN_MILLIS*10);
        return sdf.format(date);

    }
}
