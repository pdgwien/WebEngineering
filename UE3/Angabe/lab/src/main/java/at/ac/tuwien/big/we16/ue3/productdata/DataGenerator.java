package at.ac.tuwien.big.we16.ue3.productdata;

public class DataGenerator {
    public void generateData() {
        generateUserData();
        generateProductData();
        insertRelatedProducts();
    }

    private void generateUserData() {
        // TODO add the computer user to the database
    }

    private void generateProductData() {
        // TODO load products via JSONDataLoader and write them to the database
    }

    private void insertRelatedProducts() {
        // TODO load related products from dbpedia and write them to the database
    }
}
