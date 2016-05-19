package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we.dbpedia.api.DBPediaService;
import at.ac.tuwien.big.we.dbpedia.api.SelectQueryBuilder;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPedia;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPediaOWL;
import at.ac.tuwien.big.we16.ue3.exception.ProductNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.Bid;
import at.ac.tuwien.big.we16.ue3.model.Product;
import at.ac.tuwien.big.we16.ue3.model.RelatedProduct;
import at.ac.tuwien.big.we16.ue3.model.User;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.query.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;


public class ProductService {

    private EntityManager em;
    private static final Logger logger = LogManager.getLogger(ProductService.class);
    private TypedQuery<Product> query;

    public ProductService() {
        this.em = EntityManagerFactoryService.getEntityManager();
        this.query = this.em.createQuery("SELECT p FROM Product p", Product.class);
    }

    public Collection<Product> getAllProducts() {
        return this.query.getResultList();
    }

    public void createProduct(Product product) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(product);
            tx.commit();
            logger.info("Persisted product {} with ID {}", product.getName(), product.getId());
        } catch (Exception e) {
            logger.error("Error while persisting: {}", e);
            tx.rollback();
        }
    }

    public Product getProductById(String id) throws ProductNotFoundException {
        logger.info("Search for product with id {}", id);
        Product product = em.find(Product.class, id);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        return product;
    }

    //TODO: write changed users and products to db
    public Collection<Product> checkProductsForExpiration() {
        logger.info("Checking for expired products");
        Collection<Product> newlyExpiredProducts = new ArrayList<>();
        this.getAllProducts().stream().filter(product -> !product.hasExpired() && product.hasAuctionEnded()).forEach(product -> {
            product.setExpired();
            newlyExpiredProducts.add(product);
            if (product.hasBids()) {

                ServiceFactory.getBoardService().postSale(product);

                Bid highestBid = product.getHighestBid();
                for (User user : product.getUsers()) {
                    user.decrementRunningAuctions();
                    if (highestBid.isBy(user)) {
                        user.incrementWonAuctionsCount();
                    } else {
                        user.incrementLostAuctionsCount();
                    }
                }
            }
        });
        return newlyExpiredProducts;
    }

    public void loadRelatedProducts(Product product) {
        Resource person;
        SelectQueryBuilder query = DBPediaService.createQueryBuilder().setLimit(5).addPredicateExistsClause(FOAF.name).addFilterClause(RDFS.label, Locale.GERMAN);;
        switch (product.getType()) {
            case FILM:
                person = DBPediaService.loadStatements(DBPedia.createResource(product.getProducer().replace(' ', '_')));
                query = query.addWhereClause(RDF.type, DBPediaOWL.Film).addWhereClause(DBPediaOWL.director, person);
                break;
            case ALBUM:
                person = DBPediaService.loadStatements(DBPedia.createResource(product.getProducer().replace(' ', '_')));
                query = query.addWhereClause(RDF.type, DBPediaOWL.Album).addWhereClause(DBPediaOWL.artist, person);
                break;
            case BOOK:
                person = DBPediaService.loadStatements(DBPedia.createResource(product.getProducer().replace(' ', '_')));
                query = query.addWhereClause(RDF.type, DBPediaOWL.Book).addWhereClause(DBPediaOWL.author, person);
                break;
        }
        logger.info("Fetching related products for {}", product.getName());
        Model model = DBPediaService.loadStatements(query.toQueryString());
        List<String> relatedProducts = DBPediaService.getResourceNames(model, Locale.GERMAN);
        for (String name : relatedProducts) {
            RelatedProduct rel = new RelatedProduct();
            rel.setName(name);
            logger.info("Adding related product {} to {}", name, product.getName());
            product.getRelatedProducts().add(rel);
        }
    }
}
