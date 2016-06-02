package at.ac.tuwien.big.we16.ue4.productdata;

import at.ac.tuwien.big.we.dbpedia.api.DBPediaService;
import at.ac.tuwien.big.we.dbpedia.api.SelectQueryBuilder;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPedia;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPediaOWL;
import at.ac.tuwien.big.we16.ue4.error.FormError;
import at.ac.tuwien.big.we16.ue4.exception.ProductNotFoundException;
import at.ac.tuwien.big.we16.ue4.model.Product;
import at.ac.tuwien.big.we16.ue4.model.ProductType;
import at.ac.tuwien.big.we16.ue4.model.RelatedProduct;
import at.ac.tuwien.big.we16.ue4.model.User;
import at.ac.tuwien.big.we16.ue4.service.PersistenceService;
import at.ac.tuwien.big.we16.ue4.service.ProductService;
import at.ac.tuwien.big.we16.ue4.service.ServiceFactory;
import at.ac.tuwien.big.we16.ue4.service.UserService;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import java.util.*;

public class DataGenerator {
    private PersistenceService persistenceService;
    private ProductService productService;

    public DataGenerator() {
        this.persistenceService = ServiceFactory.getPersistenceService();
        this.productService = ServiceFactory.getProductService();
    }

    public void generateData() {
        generateUserData();
        generateProductData();
        //insertRelatedProducts();
    }

    private void generateUserData() {
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(1990, 5, 5);
        UserService us = new UserService(this.persistenceService);
        User u = new User("Herr","John", "Doe", "jd@example.com", "asdfasdf", dateOfBirth.getTime());
        us.createUser(u, new FormError());
        u = new User("Herr", "Jane", "Doe", "jane.doe@example.com", "asdfasdf", dateOfBirth.getTime());
        us.createUser(u, new FormError());
    }

    private void generateProductData() {
        Random r = new Random();
        for (JSONDataLoader.Music music : JSONDataLoader.getMusic()) {
            productService.createProduct(new Product(music.getAlbum_name(), music.getImg(), "", this.getRandomAuctionEndDate(r), ProductType.ALBUM ,
                    Integer.parseInt(music.getYear()), music.getArtist(), new ArrayList<>()));
        }
        for (JSONDataLoader.Movie film : JSONDataLoader.getFilms()) {
            productService.createProduct(new Product(film.getTitle(), film.getImg(), "", this.getRandomAuctionEndDate(r), ProductType.FILM,
                    Integer.parseInt(film.getYear()), film.getDirector(), new ArrayList<>()));
        }
        for (JSONDataLoader.Book book : JSONDataLoader.getBooks()) {
            productService.createProduct(new Product(book.getTitle(), book.getImg(), "", this.getRandomAuctionEndDate(r), ProductType.BOOK,
                    Integer.parseInt(book.getYear()), book.getAuthor(), new ArrayList<>()));
        }
    }

    private void insertRelatedProducts() {

        if(!DBPediaService.isAvailable())
           return;

        Collection<Product> products = productService.getAllProducts();

        for (Product product : products) {

            ProductType type = product.getType();
            String name = product.getName_en().replaceAll(" ","_");
            String producer = product.getProducer().replaceAll(" ","_");
            Resource productName = DBPediaService.loadStatements(DBPedia.createResource(name));
            Resource resProducer = DBPediaService.loadStatements(DBPedia.createResource(producer));
            product.setName_en(DBPediaService.getResourceName(productName, Locale.ENGLISH));
            product.setName_de(DBPediaService.getResourceName(productName, Locale.GERMAN));

            if (type == ProductType.ALBUM) {
                SelectQueryBuilder movieQuery = DBPediaService.createQueryBuilder()
                        .setLimit(5)
                        .addWhereClause(RDF.type, DBPediaOWL.Album)
                        .addPredicateExistsClause(FOAF.name)
                        .addWhereClause(DBPediaOWL.artist, resProducer)
                        .addMinusClause(DBPediaOWL.name, productName)
                        .addFilterClause(RDFS.label, Locale.GERMAN)
                        .addFilterClause(RDFS.label, Locale.ENGLISH);
                Model foundedProducts = DBPediaService.loadStatements(movieQuery.toQueryString());
                persistNewProductInfo(product, foundedProducts);
            }
            if (type == ProductType.BOOK) {
                SelectQueryBuilder movieQuery = DBPediaService.createQueryBuilder()
                        .setLimit(5)
                        .addWhereClause(RDF.type, DBPediaOWL.Book)
                        .addPredicateExistsClause(FOAF.name)
                        .addWhereClause(DBPediaOWL.author, resProducer)
                        .addMinusClause(DBPediaOWL.name, productName)
                        .addFilterClause(RDFS.label, Locale.GERMAN)
                        .addFilterClause(RDFS.label, Locale.ENGLISH);
                Model foundedProducts = DBPediaService.loadStatements(movieQuery.toQueryString());
                persistNewProductInfo(product, foundedProducts);
            }
            if (type == ProductType.FILM) {
                SelectQueryBuilder movieQuery = DBPediaService.createQueryBuilder()
                        .setLimit(5)
                        .addWhereClause(RDF.type, DBPediaOWL.Film)
                        .addPredicateExistsClause(FOAF.name)
                        .addWhereClause(DBPediaOWL.director, resProducer)
                        .addMinusClause(DBPediaOWL.name, productName)
                        .addFilterClause(RDFS.label, Locale.GERMAN)
                        .addFilterClause(RDFS.label, Locale.ENGLISH);
                Model foundedProducts = DBPediaService.loadStatements(movieQuery.toQueryString());
                persistNewProductInfo(product, foundedProducts);
            }
        }
    }

    private void persistNewProductInfo(Product product, Model foundedProducts) {
        List<String> englishDetails = DBPediaService.getResourceNames(foundedProducts, Locale.ENGLISH);
        List<String> germanDetails = DBPediaService.getResourceNames(foundedProducts, Locale.GERMAN);

        List<RelatedProduct> relatedProducts = new ArrayList<>();
        for(int i = 0; i < englishDetails.size(); i++) {
            RelatedProduct relatedProduct = new RelatedProduct();
            relatedProduct.setName_en(englishDetails.get(i));
            relatedProduct.setName_de(germanDetails.get(i));
            productService.createRelatedProduct(relatedProduct);
            relatedProducts.add(relatedProduct);
        }
        product.setRelatedProducts(relatedProducts);

        try {
            productService.updateProduct(product, product.getId());
        } catch (ProductNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a Date that is between 30 and 600 seconds from now.
     */
    private Date getRandomAuctionEndDate(Random r) {
        return new Date(Calendar.getInstance().getTimeInMillis() + ((r.nextInt(70) + 30) * 1000));
        //return new Date(Calendar.getInstance().getTimeInMillis() + ((r.nextInt(570) + 30) * 1000));
    }
}
