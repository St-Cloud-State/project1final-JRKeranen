import java.util.*;
import java.io.*;

public class Warehouse implements Serializable {
  private static final long serialVersionUID = 1L;
  private ProductList productList;
  private ClientList clientList;
  private static Warehouse warehouse;

  private Warehouse() {
    productList = ProductList.instance();
    clientList = ClientList.instance();
  }

  public static Warehouse instance() {
    if (warehouse == null) {
      // MemberIdServer.instance(); // instantiate all singletons
      return (warehouse = new Warehouse());
    } else {
      return warehouse;
    }
  }

  public Product addProduct(String name, int quantity, String manufacturer, float price) {
    Product product = new Product(name, quantity, manufacturer, price);
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
    } while (iterator.hasNext());
    return false;
  }

  public void DisplayClients() {
    Iterator<Client> iterator = clientList.getClients();
    do {
      Client client = iterator.next();
      System.out.println(client);
    } while (iterator.hasNext());
  }

  public void DisplayProducts() {
    Iterator<Product> iterator = productList.getProducts();
    do {
      Product product = iterator.next();
      System.out.println(product);
    } while (iterator.hasNext());
  }

  public void DisplayClientWishlist(Client client) {
    Iterator<WishItem> iterator = client.getWishItems();
    System.out.println("Wish List");
    System.out.println("--------------------------------");
    if (!iterator.hasNext()) {
      System.out.println("No Items");
    }
    while (iterator.hasNext()) {
      System.out.println(iterator.next());
    }
    System.out.println("--------------------------------");

  }

  public Boolean ProductExists(Product searchProduct) {
    Iterator<Product> iterator = productList.getProducts();
    do {
      Product product = iterator.next();
      if (product.getID() == searchProduct.getID()) {
        return true;
      }
    } while (iterator.hasNext());
    return false;
  }

  public Client searchClientByID(String id) {
    Iterator<Client> iterator = clientList.getClients();
    do {
      Client client = iterator.next();
      if (client.getId().equals(id)) {
        return client;
      }
    } while (iterator.hasNext());
    return null;
  }

  public Product searchProductByID(String id) {
    Iterator<Product> iterator = productList.getProducts();
    do {
      Product product = iterator.next();
      if (product.getID().equals(id)) {
        return product;
      }
    } while (iterator.hasNext());
    return null;
  }

  public boolean CompleteOrder(Client client) {
    Iterator<WishItem> iterator = client.getWishItems();
    float total = client.getWishListTotal();
    client.subBalance(total);
    while (iterator.hasNext()) {
      WishItem wishItem = iterator.next();
      Product product = wishItem.getProduct();
      int orderQuantity = wishItem.getQuantity();
      int stockQuantity = product.getQuantity();
      if (orderQuantity <= stockQuantity) {
        stockQuantity -= orderQuantity;
        product.setQuantity(stockQuantity);
        GenerateInvoice(client, product, orderQuantity);
      } else if (stockQuantity <= 0) {
        // only add to waitlist
        WaitItem waitItem = new WaitItem(client, product, orderQuantity);
        product.addToWaitList(waitItem);
      } else {
        int invoiceQuantity = stockQuantity;
        int waitListQuantity = orderQuantity - stockQuantity;
        product.setQuantity(0);
        GenerateInvoice(client, product, invoiceQuantity);
        WaitItem waitItem = new WaitItem(client, product, waitListQuantity);
        product.addToWaitList(waitItem);
      }
    }
    return true;
  }

  public boolean GenerateInvoice(Client client, Product product, int quantity) {
    Invoice invoice = new Invoice(client, product, quantity);
    client.addToInvoiceList(invoice);
    return true;
  }

  // New method
  public Boolean ReceiveShipment(Product product, int quantity) {
    Iterator<WaitItem> iterator = product.getWaitList();
    int total = quantity;

    while (iterator.hasNext() && (total > 0)) {
      WaitItem waitItem = iterator.next();
      Client client = waitItem.getClient();
      int waitQuantity = waitItem.getQuantity();
      if (waitQuantity <= total) {
        total -= waitQuantity;
        product.RemoveWaitItem(waitItem);
        GenerateInvoice(client, product, waitQuantity);
      } else {
        int leftoverQuantity = waitQuantity - total;
        waitItem.setQuantity(leftoverQuantity);
        GenerateInvoice(client, product, total);
        total = 0;
      }
      if (total > 0) {
        product.setQuantity(product.getQuantity() + total);
      }
    }

    return true;
  }

  public void DisplayProductWaitlist(Product product) {
    Iterator<WaitItem> iterator = product.getWaitList();
    System.out.println("----------------------");
    if (!iterator.hasNext()) {
      System.out.println("Client has no waitlist");
    }
    while (iterator.hasNext()) {
      System.out.println(iterator.next());
    }
    System.out.println("----------------------");
  }

  public void DisplayInvoices(Client client) {
    Iterator<Invoice> iterator = client.getInvoices();
    if (!iterator.hasNext()) {
      System.out.println("Client has No invoices");
    }
    while (iterator.hasNext()) {
      System.out.println(iterator.next());
    }
  }

  public boolean processPayment(Client client, float amount) {

    // Check if client is found
    if (client != null) {
      // Process the payment by debiting the client's account
      client.addToBalance(amount);
      Float bal = client.getBalance();
      System.out.println("Final Balance = $" + bal);
      return true;
    } else {
      System.out.println("Client not found.");
      return false;
    }
  }

}
