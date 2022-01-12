package tw.com.teiulin.pricecalc;

import java.math.BigDecimal;
import java.util.List;

public class Calculator {

    private final List<Promotion> promotions;

    public Calculator(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public void execute(Order order) {
        order.setTotalPrice(order.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        BigDecimal discountPrice = BigDecimal.ZERO;
        for(var promotion : promotions) {
            discountPrice = discountPrice.add(promotion.execute(order));
        }
        order.setDiscountPrice(discountPrice);
    }
}
