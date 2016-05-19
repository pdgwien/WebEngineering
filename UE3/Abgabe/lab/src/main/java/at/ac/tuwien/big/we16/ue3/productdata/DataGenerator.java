package at.ac.tuwien.big.we16.ue3.productdata;

import at.ac.tuwien.big.we16.ue3.model.Product;
import at.ac.tuwien.big.we16.ue3.model.ProductType;
import at.ac.tuwien.big.we16.ue3.model.User;
import at.ac.tuwien.big.we16.ue3.service.ProductService;
import at.ac.tuwien.big.we16.ue3.service.ServiceFactory;
import at.ac.tuwien.big.we16.ue3.service.UserService;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {
    static final long MINUTE_IN_MILLIS = 60000;

    public void generateData() {
        generateUserData();
        generateProductData();
        insertRelatedProducts();
    }

    private void generateUserData() {
        UserService userService = ServiceFactory.getUserService();
        User computer = new User();
        computer.setEmail("siri@apple.com");
        computer.setSalutation("Ms.");
        computer.setFirstname("Siri");
        computer.setLastname("Apple");
        computer.setPassword("apple123");
        computer.setBalance(999999999);
        computer.setDate(new Date(500000000));
        userService.createUser(computer);
        User testUser = new User();
        testUser.setEmail("test@test.com");
        testUser.setSalutation("Herr");
        testUser.setFirstname("Test");
        testUser.setLastname("Test");
        testUser.setPassword("test123");
        testUser.setBalance(150000);
        testUser.setDate(new Date(500000000));
        userService.createUser(testUser);
    }

    private void generateProductData() {
        ProductService productService = ServiceFactory.getProductService();
        for (JSONDataLoader.Music m : JSONDataLoader.getMusic()) {
            Product a = new Product();
            a.setImage(m.getImg());
            a.setProducer(m.getArtist());
            a.setImageAlt(m.getAlbum_name());
            a.setName(m.getAlbum_name());
            a.setYear(Integer.parseInt(m.getYear()));
            a.setType(ProductType.ALBUM);
            a.setAuctionEnd(buildExpirationDate());
            productService.createProduct(a);
        }
        for (JSONDataLoader.Book m : JSONDataLoader.getBooks()) {
            Product a = new Product();
            a.setImage(m.getImg());
            a.setProducer(m.getAuthor());
            a.setImageAlt(m.getTitle());
            a.setName(m.getTitle());
            a.setYear(Integer.parseInt(m.getYear()));
            a.setType(ProductType.BOOK);
            a.setAuctionEnd(buildExpirationDate());
            productService.createProduct(a);
        }
        for (JSONDataLoader.Movie m : JSONDataLoader.getFilms()) {
            Product a = new Product();
            a.setImage(m.getImg());
            a.setProducer(m.getDirector());
            a.setImageAlt(m.getTitle());
            a.setName(m.getTitle());
            a.setYear(Integer.parseInt(m.getYear()));
            a.setType(ProductType.FILM);
            a.setAuctionEnd(buildExpirationDate());
            productService.createProduct(a);
        }
    }

    private void insertRelatedProducts() {
        ProductService productService = ServiceFactory.getProductService();
        productService.getAllProducts().stream().forEach(productService::loadRelatedProducts);
    }

    public static Date buildExpirationDate() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date(cal.getTimeInMillis() + ThreadLocalRandom.current().nextLong(5 * MINUTE_IN_MILLIS, 20 * MINUTE_IN_MILLIS));
        return date;

    }
}
