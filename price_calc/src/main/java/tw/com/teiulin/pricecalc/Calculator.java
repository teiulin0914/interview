package tw.com.teiulin.pricecalc;

import java.math.BigDecimal;
import java.util.*;

public class Calculator {

    private final List<Promotion> promotions;

    public Calculator(List<Promotion> promotions) {
        this.promotions = new ArrayList<>(promotions);
        Collections.sort(this.promotions);
    }

    public void execute(Order order) {
        order.setTotalPrice(order.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        Set<String> flagTmp = new HashSet<>();

        BigDecimal discountPrice = BigDecimal.ZERO;
        for(var promotion : promotions) {

            if(promotion.getUniqueFlag() != null) {

                if(flagTmp.contains(promotion.getUniqueFlag())) {
                    continue;

                } else {
                    flagTmp.add(promotion.getUniqueFlag());
                }
            }

            discountPrice = discountPrice.add(promotion.execute(order));
        }
        order.setDiscountPrice(discountPrice);
    }
}
