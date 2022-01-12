package tw.com.teiulin.pricecalc;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {

    private String id;
    private String code;
    private String name;
    private BigDecimal price;

    public Product(String code, String name, BigDecimal price) {
        this.id = UUID.randomUUID().toString();
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
