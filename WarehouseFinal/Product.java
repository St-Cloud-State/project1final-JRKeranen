import java.util.*;
import java.io.*;

public class Product {
    private String id;
    private String name;
    private int quantity;
    private String manufacturer;
    private float price;
    private List waitList = new LinkedList<WaitItem>();
    private static int idNum = 1;

    public Product(String name, int quantity, String manufacturer, float price) {
        this.id = "P-" + Integer.toString(idNum);
        this.name = name;
        this.quantity = quantity;
        this.manufacturer = manufacturer;
        this.price = price;
        idNum += 1;
    }

    public WaitItem addToWaitList(WaitItem item) {
        try {
            waitList.add(item);
            return item;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean RemoveWaitItem(WaitItem waitItem) {
        waitList.remove(waitItem);
        return true;
    }

    public Iterator getWaitList() {
        return waitList.iterator();
    }

    public String getID() {
        return this.id;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getName() {
        return this.name;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public float getPrice() {
        return this.price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String toString() {
        String string = "ID: " + this.id + ", Name: " + this.name + ", Quantity: " + this.quantity + ", Manufacture: "
                + this.manufacturer + ", Price: " + this.price;
        return string;
    }
}