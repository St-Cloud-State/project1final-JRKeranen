import java.util.*;
import java.io.*;

public class ProductList{
    private LinkedList<Product> products = new LinkedList<Product>();
    private static ProductList productList;
    private ProductList() {
    }
    public static ProductList instance() {
        if (productList == null) {
          return (productList = new ProductList());
        } else {
          return productList;
        }
      }

    public Product search(String Productid){
        Product target = null;
    
        for (Product p : products) {
            if (p.getID().equals(Productid)) {
                target = p;
                return target;
            }
        }
        return null;
    }

    public boolean insertProduct(Product product){
        if(!this.products.add(product)){
            return false;
        };
        return true;
    }

    public boolean RemoveProduct(String productID){
        for(Product p: this.products){
            if(p.getID().equals(productID)){
                this.products.remove(p);
                return true;
            }
        }
        return false;
    }

    public Iterator getProducts(){
        return this.products.iterator();
    }

}
