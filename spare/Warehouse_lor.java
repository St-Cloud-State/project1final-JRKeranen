package WarehouseStage1;
import java.util.*;
import java.io.*;
import WarehouseStage1.*;

public class Warehouse implements Serializable {
  private static final long serialVersionUID = 1L;
  private ProductList productList;
  private ClientList clientList;
  private static Warehouse warehouse;
  public Warehouse() {
    productList = ProductList.instance();
    clientList = ClientList.instance();
  }
  public static Warehouse instance() {
    if (warehouse == null) {
      //MemberIdServer.instance(); // instantiate all singletons
      return (warehouse = new Warehouse());
    } else {
      return warehouse;
    }
  }
  public Product addProduct(String name,int quantity,String manufacturer,float price) {
    Product product = new Product(name,quantity,manufacturer,price);
    if (productList.insertProduct(product)) {
      return (product);
    }
    return null;
  }
  public Client addClient(String name) {
    Client client = new Client(name);
    if (clientList.insertClient(client)) {
      return (client);
    }
    return null;
  }

  public WishItem addProductToWishlist(Client client, Product product, int quantity) {
    WishItem item = new WishItem(client, product, quantity);
    WishItem result;
    result = client.addToWishList(item);
    return result;
  }

  public Boolean ClientExists(Client searchClient) {
    Iterator<Client> iterator = clientList.getClients();
    do {
      Client client = iterator.next();
      if (client.getId() == searchClient.getId()) {
        return true;
      }
    }
    while(iterator.hasNext());
    return false;
  }

  public Boolean ProductExists(Product searchProduct) {
    Iterator<Product> iterator = productList.getProducts();
    do {
      Product product = iterator.next();
      if (product.getID() == searchProduct.getID()) {
        return true;
      }
    }
    while(iterator.hasNext());
    return false;
  }

  public Client searchClientByID(String id) {
    System.out.println("Entered function");
    Iterator<Client> iterator = clientList.getClients();
    do {
      Client client = iterator.next();
      System.out.println("ran");
      System.out.println(client.getId() + ", " + id);
      if (client.getId().equals(id)) {
        return client;
      }
    }
    while(iterator.hasNext());
    return null;
  }

  public Product searchProductByID(String id) {
    Iterator<Product> iterator = productList.getProducts();
    do {
      Product product = iterator.next();
      if (product.getID().equals(id)) {
        return product;
      }
    }
    while(iterator.hasNext());
    return null;
  }

  // New method
  public Boolean ReceiveShipment(String productID, int quantity){
    // Get all clients
    System.out.println("Receiving shipment...");

    Iterator<Client> it = clientList.getClients();

    // While loop for iterating all clients
    // Exit conditions are that if there are more clients then continue
    System.out.println("Fulfilling client wishlist...");
    while(it.hasNext()){
      // Get individual client account
      Client client = it.next();

      // Grab waitlist of the client
      Iterator<WishItem> wishiter = client.getWishItems();

      // Iterate the wish list and fulfill each item that matches product ID
      // While loop exit conditions are:
      // If there are more items and the shipment quantity is
      // Greater than 0 then continue iterating
      while(wishiter.hasNext() && quantity > 0){
        WishItem item = wishiter.next();  // Grab an item from the wish list
        String searchID = item.getProduct().getID(); // Extract the ID

        if(searchID.equals(productID)){ // Check if wish item and received shipment item are the same product
          if(item.getQuantity() <= quantity){
            quantity = quantity - item.getQuantity();
          }
          else if(item.getQuantity() > quantity){
            int result = item.getQuantity() - quantity;
            item.setQuantity(result);
            quantity = 0;
            System.out.println("Shipment items ran out while fulfilling wishlists.");
            return true;
          }
        }
      }
    }

    // Add remainder to warehouse inventory
    System.out.println("Adding remainder shipment to inventory...");
    Product product = searchProductByID(productID);
    int total = quantity + product.getQuantity();
    product.setQuantity(total);
    System.out.println("Shipment received.");
    return true;
  }
}
