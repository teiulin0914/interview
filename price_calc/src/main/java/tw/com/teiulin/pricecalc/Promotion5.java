package tw.com.teiulin.pricecalc;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * （加分題）訂單滿 X 元折 Z %，折扣每人只能總共優惠 N 元
 */
public class Promotion5 extends Promotion {

    private final Map<String, BigDecimal> discountRecordPerUser;

    private final BigDecimal amount;
    private final Integer discountPercent;
    private final BigDecimal discountLimit;

    public Promotion5(BigDecimal amount, Integer discountPercent, BigDecimal discountLimit) {
        this.amount = amount;
        this.discountPercent = discountPercent;
        this.discountLimit = discountLimit;
        this.discountRecordPerUser = new ConcurrentHashMap<>();
    }

    @Override
    public BigDecimal execute(Order order) {

        if(order.getTotalPrice().compareTo(amount) >= 0) {
            BigDecimal record = discountRecordPerUser.get(order.getUserId());

            if(record == null) {
                record = discountLimit;
            }

            BigDecimal discountAmount = order.getTotalPrice()
                    .multiply(new BigDecimal(discountPercent.toString()))
                    .divide(new BigDecimal("100"));

            BigDecimal rest = record.subtract(discountAmount);

            if(rest.compareTo(BigDecimal.ZERO) < 0) {
                discountRecordPerUser.put(order.getUserId(), BigDecimal.ZERO);
                return record;

            } else {
                discountRecordPerUser.put(order.getUserId(), rest);
                return discountAmount;
            }
        }

        return BigDecimal.ZERO;
    }
}
