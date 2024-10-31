import java.util.*;
import java.text.*;
import java.io.*;

public class UserInterface {
  private static UserInterface userInterface;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int ADD_CLIENT = 1;
  private static final int ADD_PRODUCT = 2;
  private static final int ADD_PODUCT_TO_CLIENT_WISHLIST = 3;
  private static final int PLACE_CLIENT_ORDER = 4;
  private static final int RECIEVE_SHIPMENT = 5;
  private static final int MAKE_PAYMENT = 6;
  private static final int DISPLAY_ALL_CLIENTS = 7;
  private static final int DISPLAY_ALL_PRODUCTS = 8;
  private static final int DISPLAY_CLIENT_WISHLIST = 9;
  private static final int DISPLAY_CLIENT_INVOICES = 10;
  private static final int DISPLAY_PRODUCT_WAITLIST = 11;
  private static final int HELP = 12;

  private UserInterface() {
    warehouse = Warehouse.instance();
  }

  public static UserInterface instance() {
    if (userInterface == null) {
      return userInterface = new UserInterface();
    } else {
      return userInterface;
    }
  }

  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }

  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }

  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("\nEnter command: "));
        if (value >= EXIT && value <= HELP) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public int getNumber(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Integer num = Integer.valueOf(item);
        return num.intValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number ");
      }
    } while (true);
  }

  public Float getFloat(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Float num = Float.valueOf(item);
        return num.floatValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number ");
      }
    } while (true);
  }

  public void help() {
    System.out.println("Enter a number between 0 and 10 as explained below:");
    System.out.println(EXIT + " - Exit");
    System.out.println(ADD_CLIENT + " - add a client");
    System.out.println(ADD_PRODUCT + " - add a product");
    System.out.println(ADD_PODUCT_TO_CLIENT_WISHLIST + " - add product to clients wishlist");
    System.out.println(PLACE_CLIENT_ORDER + " - Place a client order");
    System.out.println(RECIEVE_SHIPMENT + " - Revieve Shipment");
    System.out.println(MAKE_PAYMENT + " - Make Payment");
    System.out.println(DISPLAY_ALL_CLIENTS + " - Display all Clients");
    System.out.println(DISPLAY_ALL_PRODUCTS + " - Display all products");
    System.out.println(DISPLAY_CLIENT_WISHLIST + " - Display client wishlist");
    System.out.println(DISPLAY_CLIENT_INVOICES + " - Display client invoices");
    System.out.println(DISPLAY_PRODUCT_WAITLIST + " - Display product WaitList");
  }

  public void addClient() {
    String name = getToken("Enter Client name");
    Client result;
    result = warehouse.addClient(name);
    Boolean test = warehouse.ClientExists(result);

    if ((result == null) || (test == false)) {
      System.out.println("Could not add Client");
    }
    System.out.println(result);
  }

  public void addProducts() {
    Product result;
    do {
      String name = getToken("Enter  name");
      String manufacturer = getToken("Enter manufacturer");
      int quantity = getNumber("Enter quantity");
      Float price = getFloat("Enter price");
      result = warehouse.addProduct(name, quantity, manufacturer, price);
      Boolean test = warehouse.ProductExists(result);
      System.out.println(test);
      if ((result != null) || (test == false)) {
        System.out.println(result);
      } else {
        System.out.println("Product could not be added");
      }
      if (!yesOrNo("Add more Products?")) {
        break;
      }
    } while (true);
  }

  public void DisplayAllClients() {
    warehouse.DisplayClients();
  }

  public void DisplayAllProducts() {
    warehouse.DisplayProducts();
  }

  public void DisplayClientWishlist() {
    try {
      String clientID = getToken("Enter Client ID: ");
      Client client = warehouse.searchClientByID(clientID);
      if (clientID == null) {
        System.out.println("Client Not Found.");
      }
      warehouse.DisplayClientWishlist(client);
    } catch (Exception e) {
      System.out.println("Error, Retry...");
    }
  }

  public void DisplayClientInvoices() {
    try {
      String clientID = getToken("Enter Client ID: ");
      Client client = warehouse.searchClientByID(clientID);
      warehouse.DisplayInvoices(client);
    } catch (Exception e) {
      System.out.println("Error,, Try Again");
    }

  }

  public void PlaceClientOrder() {
    try {
      String clientID = getToken("Enter Client ID: ");
      Client client = warehouse.searchClientByID(clientID);
      if (clientID == null) {
        System.out.println("Client Not Found.");
      }
      Iterator<WishItem> iterator = client.getWishItems();

      if (!iterator.hasNext()) {
        System.out.println("Client has nothing in the wishlist...");
      }
      System.out.println("Starting Wishlist");
      warehouse.DisplayClientWishlist(client);

      while (iterator.hasNext()) {
        WishItem wishItem = iterator.next();
        System.out.println("-------------------------------------");
        System.out.println(wishItem);
        System.out.println("-------------------------------------");
        System.out.println("1 - Remove Item from the wishlist");
        System.out.println("2 - Change Item Quantity");
        System.out.println("3 - Next Item");
        String option = getToken("Enter Option: ");

        switch (option) {
          case "1":
            client.RemoveWishItem(wishItem);
            System.out.println("removing wish Item... ");
            break;
          case "2":
            int newQuantity = getNumber("Enter new quantity: ");
            wishItem.setQuantity(newQuantity);
            break;
          case "3":
            System.out.println("Going to next Item...");
            break;

          default:
            System.out.println("Invalid Option, Restart");
            PlaceClientOrder();
            break;
        }
      }

      warehouse.DisplayClientWishlist(client);
      warehouse.CompleteOrder(client);
    } catch (Exception e) {

      System.out.println(e + "Error, Retry...");
    }
  }

  public void ReceiveShipment() {
    try {
      do {
        String productID = getToken("Enter product id you are recieving");
        Product product = warehouse.searchProductByID(productID);
        if (product == null) {
          System.out.println("Could not find product, might need to add new product.");
          return;
        }
        int Quantity = getNumber("Enter Product Quantity: ");
        if (Quantity > 0) {
          warehouse.ReceiveShipment(product, Quantity);
        } else {
          System.out.println("Quantity must be greater than 0");
        }

        if (!yesOrNo("Recive more Products?")) {
          break;
        }
      } while (true);

    } catch (Exception e) {
      System.out.println(e + "Error");
    }
  }

  public void MakePayment() {
    try {
      String clientID = getToken("Enter Client ID: ");
      Client client = warehouse.searchClientByID(clientID);
      if (clientID == null) {
        System.out.println("Client Not Found.");
      }

      float paymentAmount = getFloat("Enter Payment Amount: ");
      System.out.println("Beginning Balance: $" + client.getBalance());
      boolean success = warehouse.processPayment(client, paymentAmount);
      if (success == true) {
        System.out.println("Payment Completed");
      }
    } catch (Exception e) {
      System.out.println("Error, Retry...");
    }
  }

  public void addProductToWishlist() {
    String cid = getToken("Enter client id");
    do {
      try {
        Client client;
        client = warehouse.searchClientByID(cid);
        if (client == null) {
          System.out.println("Client With id not found");
          return;
        }

        String pid = getToken("Enter product id you want to add");
        int quantity = getNumber("Enter quantity");

        Product product;
        product = warehouse.searchProductByID(pid);

        if (product == null) {
          System.out.println("product With id not found");
          return;
        }

        WishItem item;
        item = warehouse.addProductToWishlist(client, product, quantity);

        if (item == null) {
          System.out.println("There was an error add product to wishlist");
        }
        System.out.println(item);

        if (!yesOrNo("Add more Products to wishlist?")) {
          break;
        }
      } catch (Exception err) {
        System.out.println("There was an error add product to wishlist");
      }
    } while (true);
  }
  public void DisplayProductWaitlist() {
    try {
      String productID = getToken("Enter Client ID: ");
      Product product = warehouse.searchProductByID(productID);
      warehouse.DisplayProductWaitlist(product);
    } catch (Exception e) {
      System.out.println("Error,, Try Again");
    }
  }
  public void process() {
    int command;
    help();
    while ((command = getCommand()) != EXIT) {
      switch (command) {
        case ADD_CLIENT:
          addClient();
          break;
        case ADD_PRODUCT:
          addProducts();
          break;
        case ADD_PODUCT_TO_CLIENT_WISHLIST:
          addProductToWishlist();
          break;
        case PLACE_CLIENT_ORDER:
          PlaceClientOrder();
          break;
        case RECIEVE_SHIPMENT:
          ReceiveShipment();
          break;
        case MAKE_PAYMENT:
          MakePayment();
          break;
        case DISPLAY_ALL_CLIENTS:
          DisplayAllClients();
          break;
        case DISPLAY_ALL_PRODUCTS:
          DisplayAllProducts();
          break;
        case DISPLAY_CLIENT_WISHLIST:
          DisplayClientWishlist();
          break;
        case DISPLAY_CLIENT_INVOICES:
          DisplayClientInvoices();
          break;
        case DISPLAY_PRODUCT_WAITLIST:
          DisplayProductWaitlist();
          break;
        case HELP:
          help();
          break;
      }
    }
  }

  public static void main(String[] s) {
    UserInterface.instance().process();
  }
}
