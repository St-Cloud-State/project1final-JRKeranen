public class Invoice {
    private Client client; // The client adding items to their wishlist
    private Product product; // The product they are adding
    private int quantity;
    private float total; // The quantity of the product desired

    // Constructor to initialize the WishItem
    public Invoice(Client client, Product product, int quantity) {
        this.client = client;
        this.product = product;
        this.quantity = quantity;
        this.total = this.product.getPrice() * this.quantity;
    }

    // Getters for the fields
    public Client getClient() {
        return client;
    }

    public Product getProduct() {
        return this.product;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public float getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "Purchased" + this.quantity + ", " + this.product.getName() + ", with a total of $" + this.total;
    }
}