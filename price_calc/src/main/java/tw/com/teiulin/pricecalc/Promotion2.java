package tw.com.teiulin.pricecalc;

import java.math.BigDecimal;
import java.util.List;

/**
 * 特定商品滿 X 件折 Y 元
 */
public class Promotion2 extends Promotion {

    private final List<String> productCodes;
    private final Long count;
    private final BigDecimal discountAmount;

    public Promotion2(List<String> productCodes, Long count, BigDecimal discountAmount) {
        this.productCodes = productCodes;
        this.count = count;
        this.discountAmount = discountAmount;
    }

    @Override
    public BigDecimal execute(Order order) {

        long meetCount = order.getProducts().stream()
                .filter(product -> productCodes.contains(product.getCode()))
                .count();

        if(meetCount >= count) {
            return discountAmount;
        }

        return BigDecimal.ZERO;
    }
}
