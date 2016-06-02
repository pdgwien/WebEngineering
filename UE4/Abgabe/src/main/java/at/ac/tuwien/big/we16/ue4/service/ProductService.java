package at.ac.tuwien.big.we16.ue4.service;

import at.ac.tuwien.big.we16.ue4.exception.ProductNotFoundException;
import at.ac.tuwien.big.we16.ue4.model.Bid;
import at.ac.tuwien.big.we16.ue4.model.Product;
import at.ac.tuwien.big.we16.ue4.model.RelatedProduct;
import at.ac.tuwien.big.we16.ue4.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProductService {
    private final PersistenceService persistenceService;

    public ProductService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void createProduct(Product product) {
        EntityManager em = this.persistenceService.getEntityManager();
        em.getTransaction().begin();
        em.persist(product);
        em.getTransaction().commit();
        em.close();
    }

    public void createRelatedProduct(RelatedProduct product) {
        EntityManager em = this.persistenceService.getEntityManager();
        em.getTransaction().begin();
        em.persist(product);
        em.getTransaction().commit();
        em.close();
    }

    public void updateProduct(Product product, String idToUpdate) throws ProductNotFoundException {
        EntityManager em = this.persistenceService.getEntityManager();
        Product p = em.find(Product.class, idToUpdate);
        em.getTransaction().begin();
        p.setAllValues(product);
        em.getTransaction().commit();
        em.close();
    }


    public Collection<Product> getAllProducts() {
        EntityManager em = this.persistenceService.getEntityManager();
        em.getTransaction().begin();
        TypedQuery<Product> query = em.createQuery("select p from Product p", Product.class);
        List<Product> result = query.getResultList();
        em.getTransaction().commit();
        em.close();
        return result;
    }

    public Product getProductById(String id) throws ProductNotFoundException {
        EntityManager em = this.persistenceService.getEntityManager();
        em.getTransaction().begin();
        Product product = em.find(Product.class, id);
        em.getTransaction().commit();
        em.close();
        if (product == null) {
            throw new ProductNotFoundException();
        }
        return product;
    }

    public Collection<Product> checkProductsForExpiration() {
        Collection<Product> newlyExpiredProducts = new ArrayList<>();
        EntityManager em = this.persistenceService.getEntityManager();
        em.getTransaction().begin();
        for (Product product : this.getAllProducts()) {
            if (!product.hasExpired() && product.hasAuctionEnded()) {
                product.setExpired();
                em.merge(product);
                newlyExpiredProducts.add(product);
                if (product.hasBids()) {
                    Bid highestBid = product.getHighestBid();
                    for (User user : product.getUsers()) {
                        user.decrementRunningAuctions();
                        if (highestBid.isBy(user)) {
                            user.incrementWonAuctionsCount();
                        }
                        else {
                            user.incrementLostAuctionsCount();
                        }
                        em.merge(user);
                    }
                }
            }
        }
        em.getTransaction().commit();
        em.close();
        return newlyExpiredProducts;
    }
}
