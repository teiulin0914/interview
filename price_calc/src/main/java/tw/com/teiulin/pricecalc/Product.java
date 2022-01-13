package tw.com.teiulin.pricecalc;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {

    private String id;
    private String code;
    private String name;
    private BigDecimal price;

    public Product() {}

    public Product(String code, String name, BigDecimal price) {
        this.id = UUID.randomUUID().toString();
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
