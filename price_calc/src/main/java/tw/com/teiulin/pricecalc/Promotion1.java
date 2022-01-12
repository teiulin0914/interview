package tw.com.teiulin.pricecalc;

import java.math.BigDecimal;

/**
 * 訂單滿 X 元折 Z %
 */
public class Promotion1 extends Promotion {

    private final BigDecimal amount;
    private final Integer discountPercent;

    public Promotion1(BigDecimal amount, Integer discountPercent) {
        this.amount = amount;
        this.discountPercent = discountPercent;
    }

    @Override
    public BigDecimal execute(Order order) {

        if(order.getTotalPrice().compareTo(amount) >= 0) {
            return order.getTotalPrice()
                    .multiply(new BigDecimal(discountPercent.toString()))
                    .divide(new BigDecimal("100"));
        }

        return BigDecimal.ZERO;
    }
}
