public class WaitItem {
    private Client client; // The client adding items to their wishlist
    private Product product; // The product they are adding
    private int quantity; // The quantity of the product desired

    // Constructor to initialize the WishItem
    public WaitItem(Client client, Product product, int quantity) {
        this.client = client;
        this.product = product;
        this.quantity = quantity;
    }

    // Getters for the fields
    public Client getClient() {
        return this.client;
    }

    public Product getProduct() {
        return this.product;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    @Override
    public String toString() {
        return this.client.getId() + " wished for " + this.product.getName() + " x " + this.quantity;
    }
}