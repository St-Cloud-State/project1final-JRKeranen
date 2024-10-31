import java.util.*;
import java.io.*;

public class Client {
  private float balance;
  private String name;
  private String id;
  private static int idNum = 1;
  private List wishList = new LinkedList<WishItem>();
  private List invoiceList = new LinkedList<Invoice>();

  public Client(String name) {

    this.name = name;
    id = "C-" + Integer.toString(idNum);
    idNum += 1;
    this.balance = 0;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public void setName(String newName) {
    name = newName;
  }

  public boolean equals(String id) {
    return this.id.equals(id);
  }

  public float getBalance() {
    return balance;
  }

  public WishItem addToWishList(WishItem item) {
    try {
      wishList.add(item);
      return item;
    } catch (Exception e) {
      return null;
    }
  }

  public Invoice addToInvoiceList(Invoice item) {
    try {
      invoiceList.add(item);
      return item;
    } catch (Exception e) {
      return null;
    }
  }

  public Iterator getWishItems() {
    return wishList.iterator();
  }

  public Iterator getInvoices() {
    return invoiceList.iterator();
  }

  public float addToBalance(float payment) {
    balance += payment;
    return balance;
  }

  public float subBalance(float cost) {
    balance -= cost;
    return balance;
  }

  public float getWishListTotal() {
    Iterator<WishItem> iterator = getWishItems();
    float total = 0;
    while (iterator.hasNext()) {
      WishItem wishItem = iterator.next();
      Product product = wishItem.getProduct();
      float wishprice = product.getPrice() * wishItem.getQuantity();
      total += wishprice;
    }
    return total;
  }

  public String toString() {
    String string = "ID: " + id + ", Name: " + name + ", Account Balance: $" + balance;
    return string;
  }

  public boolean RemoveWishItem(WishItem wishItem) {
    wishList.remove(wishItem);
    return true;
  }
}
