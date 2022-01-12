package tw.com.teiulin.pricecalc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private List<Product> products;
    private BigDecimal totalPrice;
    private BigDecimal discountPrice;
    private String userId;
    private String time;

    public Order(String userId, String time) {
        this.products = new ArrayList<>();
        this.totalPrice = BigDecimal.ZERO;
        this.discountPrice = BigDecimal.ZERO;
        this.userId = userId;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getUserId() {
        return userId;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getCheckoutPrice() {
        return this.totalPrice.subtract(this.discountPrice);
    }
}
